package demo;

import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class HelloController {


	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping("/hi/{name}")
	public  String hiThere(Map model, @PathVariable String name) {
		model.put("myName", name);
		return "hello";
	}
}
