import name.Example_start;
import org.junit.Assert;
import org.junit.Test;

public class Example_test {
    Example_start example = new Example_start();

    @Test
    public void task_one(){

        Assert.assertArrayEquals(new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9}, example.task1());
    }
}
