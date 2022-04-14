import com.alibaba.fastjson.annotation.JSONField;
import org.junit.Assert;
import org.junit.Assume;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import junit.framework.TestCase;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.JSONSerializer;
import com.alibaba.fastjson.serializer.SerializeWriter;
import com.alibaba.fastjson.serializer.SerializerFeature;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@RunWith(Parameterized.class)
public class MapParameterizedTest extends TestCase {
	
	enum ConfigType{
		TEST_NO_SORT, TEST_NULL, TEST_ONJSONFIELD
	}
	
	@Parameterized.Parameters
	public static Collection<Object[]> configureTest1(){
		Object[][] params= {
				{ConfigType.TEST_NO_SORT,"{'name':'jobs','id':33}", null},
				{ConfigType.TEST_NULL, "{\"name\":null}", null},
				{ConfigType.TEST_ONJSONFIELD, "{\"map\":{\"Ariston\":null}}", null}
		};
		return Arrays.asList(params);
	}
	
	private final ConfigType configType;
	private Object param1;
	private Object param2;
	
	public MapParameterizedTest(ConfigType configType, String param1, String param2){
        this.configType = configType;
		this.param1 = param1;
        this.param2 = param2;
    }
	
	@Before
	public void configureTest() throws NoSuchMethodException{
		switch(configType) {
		  case TEST_NO_SORT:
			  configureTestNoSort();
			  break;
		  case TEST_NULL:
			  configureTestNull();
			  break;
		  case TEST_ONJSONFIELD:
			  configureTestOnJSONField();
			  break;
		}
	}

	
	private void configureTestOnJSONField() {
		Map<String, String> map = new HashMap();
        map.put("Ariston", null);
        MapNullValue mapNullValue = new MapNullValue();
        mapNullValue.setMap( map );
        param2 = JSON.toJSONString( mapNullValue );
		
	}

	private void configureTestNull() {
		JSONObject obj = new JSONObject(true);
        obj.put("name", null);
        param2 = JSON.toJSONString(obj, SerializerFeature.WriteMapNullValue);		
	}

	private void configureTestNoSort() {
		JSONObject obj = new JSONObject(true);
        obj.put("name", "jobs");
        obj.put("id", 33);
        param2 = toJSONString(obj);	
	}

	@Test
    public void test_no_sort() throws Exception {
		Assume.assumeTrue(configType.equals(ConfigType.TEST_NO_SORT));
        Assert.assertEquals(param1, param2);
    }
    
	@Test
    public void test_null() throws Exception {
		Assume.assumeTrue(configType.equals(ConfigType.TEST_NULL));
		Assert.assertEquals(param1, param2);
    }

    public static final String toJSONString(Object object) {
        SerializeWriter out = new SerializeWriter();

        try {
            JSONSerializer serializer = new JSONSerializer(out);
            serializer.config(SerializerFeature.SortField, false);
            serializer.config(SerializerFeature.UseSingleQuotes, true);

            serializer.write(object);

            return out.toString();
        } catch (StackOverflowError e) {
            throw new JSONException("maybe circular references", e);
        } finally {
            out.close();
        }
    }

    @Test
    public void test_onJSONField() {
    	Assume.assumeTrue(configType.equals(ConfigType.TEST_ONJSONFIELD));
        assertEquals(param1, param2);
    }

    class MapNullValue {
        @SuppressWarnings("rawtypes")
		@JSONField(serialzeFeatures = {SerializerFeature.WriteMapNullValue})
        private Map map;

        @SuppressWarnings("rawtypes")
		public Map getMap() {
            return map;
        }

        @SuppressWarnings("rawtypes")
		public void setMap( Map map ) {
            this.map = map;
        }
    }

}