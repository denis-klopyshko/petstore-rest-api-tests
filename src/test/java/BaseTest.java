import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;

@ContextConfiguration(locations = "classpath:context.xml")
public class BaseTest extends AbstractTestNGSpringContextTests {
}
