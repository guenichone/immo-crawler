package org.barrak.immocrawler;

import org.barrak.immocrawler.crawler.ICrawler;
import org.barrak.immocrawler.crawler.criterias.SearchCriteria;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

import java.util.Collection;

@SpringBootApplication
public class ImmoCrawlerApplication {

	public static void main(String args[]) {
		SpringApplication.run(ImmoCrawlerApplication.class, args);
	}

	@Bean
	public CommandLineRunner commandLineRunner(ApplicationContext ctx) {
		return args -> {

			SearchCriteria searchCriteria = new SearchCriteria("Aumetz", 15);

			Collection<ICrawler> crawlers = ctx.getBeansOfType(ICrawler.class).values();

			for (ICrawler crawler : crawlers) {
				crawler.search(searchCriteria);
			}
		};
	}
}
