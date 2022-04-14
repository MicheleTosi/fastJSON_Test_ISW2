import java.util.Arrays;
import java.util.Collection;

import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import com.alibaba.fastjson.JSON;

import junit.framework.TestCase;

import org.junit.Test;

/**
 * Created by MicheleTosi on 2022/4/14.
 */

@RunWith(Parameterized.class)
public class FieldOrderParameterizedTest extends TestCase {
	
	@Parameterized.Parameters
	public static Collection<Object[]> configure(){
		String text="{\"name\":\"njb\",\"school\":{\"name\":\"llyz\"}}";
		Person p = new Person();
        p.setName("njb");
        School s = new School();
        s.setName("llyz");
        p.setSchool(s);
        String json = JSON.toJSONString(p);
		Object[][] params= {
				{text, json}
		};
		return Arrays.asList(params);
	}
	
	private Object param1;
	private Object param2;
	
	public FieldOrderParameterizedTest(String param1, String param2){
		this.param1 = param1;
        this.param2 = param2;
    }
	
	@Test
    public void test_field_order() throws Exception {
        assertEquals(param1, param2);
    }

    public static class Person {
        private String name;
        private School school;

        public boolean isSchool() {
            return false;
        }

        public School getSchool() {
            return school;
        }

        public void setSchool(School school) {
            this.school = school;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

    }

    public static class School {
        private String name;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}