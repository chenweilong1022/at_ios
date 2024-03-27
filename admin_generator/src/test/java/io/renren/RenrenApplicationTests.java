package io.renren;

import io.renren.service.SysGeneratorService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RenrenApplicationTests {

	@Autowired
	private SysGeneratorService sysGeneratorService;

	@Test
	public void contextLoads() {
		sysGeneratorService.generatorCode(new String[]{
				"cd_get_phone",
				"cd_register_task",
				"cd_register_subtasks",
				"cd_line_register",
		});
	}

}
