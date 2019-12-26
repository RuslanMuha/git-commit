package com.git.commit;

import org.apache.commons.io.FileUtils;
import org.kohsuke.github.*;
import org.springframework.beans.factory.annotation.Autowired;


import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Objects;


public class NewCommit {


    static public String pushFiles(File baseDirectory, String message,
                                   String userName,
                                   String repositoryName, String branchName) throws IOException {

        GitHub github = GitHub.connect("username", "token");

//        GitHub github = GitHub.connectUsingPassword(userName, password);
        GHRepository repo = github.getRepository(userName + "/" + repositoryName);
        // get current branch
        GHRef ref = repo.getRef("heads/" + branchName);
        GHCommit latestCommit = repo.getCommit(ref.getObject().getSha());
        GHTreeBuilder treeBuilder = repo.createTree().baseTree(latestCommit.getTree().getSha());
        addFilesToTree(treeBuilder, baseDirectory, baseDirectory);
        GHTree tree = treeBuilder.create();
        GHCommit commit = repo.createCommit()
                .parent(latestCommit.getSHA1())
                .tree(tree.getSha())
                .message(message)
                .create();
        ref.updateTo(commit.getSHA1());

        return commit.getSHA1();
    }

    private static void addFilesToTree(GHTreeBuilder treeBuilder, File baseDirectory, File currentDirectory) throws IOException {
        for (File file : Objects.requireNonNull(currentDirectory.listFiles())) {
            String relativePath = baseDirectory.toURI().relativize(file.toURI()).getPath();
            if (file.isFile()) {
                treeBuilder.add(relativePath, FileUtils.readFileToString(file, Charset.defaultCharset()), false);

            } else {
                addFilesToTree(treeBuilder, baseDirectory, file);
            }
        }
    }


}
