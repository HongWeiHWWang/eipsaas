package form.core;

import java.io.IOException;

import javax.annotation.Resource;

import org.junit.Test;

import com.hotent.form.generator.GeneratorModel;
import com.hotent.form.generator.GeneratorService;

public class GeneratorServiceTest extends FormTestCase{
	@Resource
	GeneratorService generatorService;
	
	@Test
	public void formGeneratorTest() throws IOException {
		GeneratorModel generatorModel = new GeneratorModel();
		generatorModel.setType(GeneratorModel.TYPE_TABLE);
		generatorModel.setAuthorName("heyf");
		generatorModel.setBasePackage("com.hotent.form");
		generatorModel.setTableName(new String[] {"ex_student"});
		String generator = generatorService.generator(generatorModel);
		System.out.println(generator);
	}
}
