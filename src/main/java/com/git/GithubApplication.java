package com.git;

import com.git.commit.NewCommit;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.File;

@SpringBootApplication
public class GithubApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(GithubApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        NewCommit.pushFiles(new File("E:\\gitFile"),"initial commit",
                "RuslanMuha","configProperties","master");
    }
}
