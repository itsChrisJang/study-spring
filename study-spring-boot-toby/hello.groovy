@RestController
class HelloController {
	@GetMapping("/")
	def hello() {
		return "Hello Spring Boot !"
	}
}

