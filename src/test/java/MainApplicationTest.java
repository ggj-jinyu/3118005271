import ggj.Main;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Unit test for simple App.
 */
public class MainApplicationTest {

    String[] paths = {
            "src/test/testcase/orig.txt",
            "src/test/testcase/empty.txt",
            "src/test/testcase/orig_0.8_add.txt",
            "src/test/testcase/orig_0.8_del.txt",
            "src/test/testcase/orig_0.8_dis_1.txt",
            "src/test/testcase/orig_0.8_dis_10.txt",
            "src/test/testcase/orig_0.8_dis_15.txt",
            "src/test/testcase/unknown.txt"
    };

    @BeforeClass
    public static void beforeTest(){
        System.out.println("测试即将开始");
    }

    @AfterClass
    public static void afterTest(){
        System.out.println("测试结束");
    }


    /**
     * 测试 文本为空文本的情况
     */
    @Test
    public void testForEmpty(){
        try {
            Main.process(paths[0],paths[1],"src/test/result/testEmptyResult.txt");
        }
        catch (Exception e) {
            e.printStackTrace();
            // 如果抛出异常，证明测试失败,没有通过，没通过的测试计数在Failures中
            Assert.fail();
        }
    }

    /**
     * 测试 输入的对比文本路径参数为错误参数的情况
     */
    @Test
    public void testForWrongOriginArgument(){
        try {
            Main.process("src/test/testcase/123.txt",paths[0],"src/test/result/testAddResult.txt");
        }
        catch (Exception e) {
            e.printStackTrace();
            // 如果抛出异常，证明测试失败,没有通过，没通过的测试计数在Failures中
            Assert.fail();
        }
    }

    /**
     * 测试20%文本添加情况：orig_0.8_add.txt
     */
    @Test
    public void testForAdd(){
        try {
            Main.process(paths[0],paths[2],"src/test/result/testAddResult.txt");
        }
        catch (Exception e) {
            e.printStackTrace();
            // 如果抛出异常，证明测试失败,没有通过，没通过的测试计数在Failures中
            Assert.fail();
        }
    }

    /**
     * 测试20%文本删除情况：orig_0.8_del.txt
     */
    @Test
    public void testForDel(){
        try {
            Main.process(paths[0],paths[3],"src/test/result/testDelResult.txt");
        }
        catch (Exception e) {
            e.printStackTrace();
            // 如果抛出异常，证明测试失败,没有通过，没通过的测试计数在Failures中
            Assert.fail();
        }
    }

    /**
     * 测试20%文本乱序情况：orig_0.8_dis_1.txt
     */
    @Test
    public void testForDis1(){
        try {
            Main.process(paths[0],paths[4],"src/test/result/testDis1Result.txt");
        }
        catch (Exception e) {
            e.printStackTrace();
            // 如果抛出异常，证明测试失败,没有通过，没通过的测试计数在Failures中
            Assert.fail();
        }
    }



    /**
     * 测试20%文本乱序情况：orig_0.8_dis_10.txt
     */
    @Test
    public void testForDis10(){
        try {
            Main.process(paths[0],paths[5],"src/test/result/testDis10Result.txt");
        }
        catch (Exception e) {
            e.printStackTrace();
            // 如果抛出异常，证明测试失败,没有通过，没通过的测试计数在Failures中
            Assert.fail();
        }
    }

    /**
     * 测试20%文本乱序情况：orig_0.8_dis_15.txt
     */
    @Test
    public void testForDis15(){
        try {
            Main.process(paths[0],paths[6],"src/test/result/testDis15Result.txt");
        }
        catch (Exception e) {
            e.printStackTrace();
            // 如果抛出异常，证明测试失败,没有通过，没通过的测试计数在Failures中
            Assert.fail();
        }
    }

    /**
     * 测试相同文本：orig.txt
     */
    @Test
    public void testForSame(){
        try {
            Main.process(paths[0],paths[0],"src/test/result/testSameResult.txt");
        }
        catch (Exception e) {
            e.printStackTrace();
            // 如果抛出异常，证明测试失败,没有通过，没通过的测试计数在Failures中
            Assert.fail();
        }
    }

    /**
     * 测试未知文本：unknown.txt
     */
    @Test
    public void testUnknown(){
        try {
            Main.process(paths[0],paths[7],"src/test/result/testUnknown.txt");
        }
        catch (Exception e) {
            e.printStackTrace();
            // 如果抛出异常，证明测试失败,没有通过，没通过的测试计数在Failures中
            Assert.fail();
        }
    }
}
