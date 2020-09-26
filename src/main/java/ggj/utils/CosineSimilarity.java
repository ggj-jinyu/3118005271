package ggj.utils;

import ggj.AtomicFloat;
import ggj.bean.Word;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import static java.lang.System.*;

public class CosineSimilarity {
    //SimilarTextCalculator Should not be instantiated，将CosineSimilarity实例化方法private
    private CosineSimilarity() {
    }

    /**
     * 计算两个字符串的相似度
     */
    public static double getSimilarity(String text1, String text2) {
        boolean isBlank1 = StringUtils.isBlank(text1);
        boolean isBlank2 = StringUtils.isBlank(text2);
        //如果内容为空，或者字符长度为0，则代表完全相同
        if (isBlank1 && isBlank2) {
            return 100;//百分制，实质为 100%，即 1.0
        }
        //如果一个为0或者空，一个不为，那说明完全不相似
        if (isBlank1 || isBlank2) {
            return 0.0;
        }
        //这个代表如果两个字符串相等那当然返回1了
        if (text1.equalsIgnoreCase(text2)) {
            return 100;
        }

        //第一步：进行分词
        List<Word> words1 = Tokenizer.string2WordList(text1);
        List<Word> words2 = Tokenizer.string2WordList(text2);

        return getSimilarity(words1, words2);//返回相似度
    }

    /**
     * 可以对于计算的相似度保留小数点后4位
     */
    public static double getSimilarity(List<Word> words1, List<Word> words2) {
        double ans = getSimilarityImpl(words1, words2);
        double ansPer = ans * 100.0;//百分制输出答案
        String exactAns = String.valueOf(ansPer);
        BigDecimal decimal = new BigDecimal(exactAns);
        //数字格式化，保留2位小数，最后一位4舍5入
        return decimal.setScale(2, RoundingMode.HALF_UP).doubleValue();
    }

    /**
     * 文本相似度计算
     * 判定方式：余弦相似度，通过计算两个向量的夹角余弦值来评估他们的相似度
     * 余弦夹角原理： 向量a=(x1,y1),向量b=(x2,y2) similarity=a.b/|a|*|b| a.b=x1x2+y1y2
     * |a|=根号[(x1)^2+(y1)^2],|b|=根号[(x2)^2+(y2)^2]
     */
    public static double getSimilarityImpl(List<Word> words1, List<Word> words2) {

        // 第一步：向每一个Word对象的属性都注入weight（权重）属性值
        taggingWeightByFrequency(words1, words2);

        //第二步：计算词频
        //通过上一步让每个Word对象都有权重值，那么在封装到map中（key是词，value是该词出现的次数（即权重））
        Map<String, Float> weightMap1 = getFastSearchMap(words1);
        Map<String, Float> weightMap2 = getFastSearchMap(words2);

        //将所有词都装入set容器中
        Set<Word> words = new HashSet<>();
        words.addAll(words1);
        words.addAll(words2);

        AtomicFloat ab = new AtomicFloat();// a.b
        AtomicFloat aa = new AtomicFloat();// |a|的平方
        AtomicFloat bb = new AtomicFloat();// |b|的平方

        // 第三步：写出词频向量，后进行计算
        words.parallelStream().forEach(word -> {
            //看同一词在a、b两个集合出现的此次
            Float x1 = weightMap1.get(word.getName());
            Float x2 = weightMap2.get(word.getName());
            if (x1 != null && x2 != null) {
                //x1x2
                float oneOfTheDimension = x1 * x2;
                ab.addAndGet(oneOfTheDimension);//给ab赋值
            }
            if (x1 != null) {
                //(x1)^2
                float oneOfTheDimension = x1 * x1;
                aa.addAndGet(oneOfTheDimension);
            }
            if (x2 != null) {
                //(x2)^2
                float oneOfTheDimension = x2 * x2;
                bb.addAndGet(oneOfTheDimension);
            }
        });
        //|a| 对aa开方
        double aaa = Math.sqrt(aa.doubleValue());
        //|b| 对bb开方
        double bbb = Math.sqrt(bb.doubleValue());

        //使用BigDecimal保证精确计算浮点数
        BigDecimal aabb = BigDecimal.valueOf(aaa).multiply(BigDecimal.valueOf(bbb));

        //similarity=a.b/|a|*|b|
        //divide参数说明：aabb被除数,9表示小数点后保留9位，最后一个表示用标准的四舍五入法
        return BigDecimal.valueOf(ab.get()).divide(aabb, 9, RoundingMode.UP).doubleValue();
    }

    /**
     * 向每一个Word对象的属性都注入weight（权重）属性值
     */
    protected static void taggingWeightByFrequency(List<Word> words1, List<Word> words2) {
        if (words1.get(0).getWeight() != null && words2.get(0).getWeight() != null) {
            return;
        }
        //词频统计（key是词，value是该词在这段句子中出现的次数）
        Map<String, AtomicInteger> frequency1 = getFrequency(words1);
        Map<String, AtomicInteger> frequency2 = getFrequency(words2);
        // 标注权重（该词出现的次数）
        words1.parallelStream().forEach(word -> word.setWeight(frequency1.get(word.getName()).floatValue()));
        words2.parallelStream().forEach(word -> word.setWeight(frequency2.get(word.getName()).floatValue()));
    }

    /**
     * 统计词频
     * @return 词频统计图
     */
    private static Map<String, AtomicInteger> getFrequency(List<Word> words) {

        Map<String, AtomicInteger> freq = new HashMap<>();
        words.forEach(i -> freq.computeIfAbsent(i.getName(), k -> new AtomicInteger()).incrementAndGet());
        return freq;
    }

    /**
     * 构造权重快速搜索容器
     */
    protected static Map<String, Float> getFastSearchMap(List<Word> words) {
        //空的词语列表
        if (words == null || words.isEmpty()) {
            return Collections.emptyMap();
        }
        Map<String, Float> weightMap = new ConcurrentHashMap<>(words.size());

        words.parallelStream().forEach(i -> {
            if (i.getWeight() != null) {
                weightMap.put(i.getName(), i.getWeight());
            } else {
                out.println("no word weight info:" + i.getName());
            }
        });
        return weightMap;
    }
}
