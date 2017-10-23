import com.takeaway.game.utils.GameUtils;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * @author abhishekrai
 * @since 0.1.0
 */

public class GameUtilsTest {

    @Test
    public void shouldReturnNumberDivisibleByThree() {
        assertEquals("Number Divisible by three", 150,
                     GameUtils.makeNumberDivisibleByThree(151));
    }
}
