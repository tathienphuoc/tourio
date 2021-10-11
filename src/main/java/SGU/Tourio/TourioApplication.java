package SGU.Tourio;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.jdbc.core.JdbcTemplate;
@SpringBootApplication
public class TourioApplication implements CommandLineRunner{
	@Autowired
	private JdbcTemplate jdbcTemplate;

	public static void main(String[] args) {
		SpringApplication.run(TourioApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		String sql = "INSERT INTO account (full_name, user_name, pwd,role_id) VALUES (?,?, ?, ?)";

		int result = jdbcTemplate.update(sql, "tathienphuoc", "tathienphuoc@gmail.com", "tathienphuoc",1);
		if (result > 0) {
			System.out.println("A new row has been inserted.");
		}

	}
}
