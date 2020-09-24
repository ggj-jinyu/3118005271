package ggj.utils;

import com.hankcs.hanlp.HanLP;
import com.hankcs.hanlp.seg.common.Term;
import ggj.bean.Word;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

public class Tokenizer {

    private Tokenizer(){
        throw new IllegalStateException("TextUtil Should not be instantiated");
    }

    /**
     * 利用HanLp分词
     */
    public static List<Word> string2WordList(String sentence) {

        //1、 采用HanLP中文自然语言处理中标准分词(兼顾精度和效率)，进行分词
        List<Term> termList = HanLP.segment(sentence);

        //2、重新封装到Word对象中（term.word代表分词后的词语，term.nature代表改词的词性）
        return termList.stream().map(term ->
                new Word(term.word, term.nature.toString())).collect(Collectors.toList());
    }

    public static String formatPrint(double ans){
        double ansPer = ans * 100.0;
        String exactAnsPer = String.valueOf(ansPer);
        BigDecimal decimal = new BigDecimal(exactAnsPer);
        return decimal.setScale(2,BigDecimal.ROUND_HALF_UP).toPlainString();
    }
}
