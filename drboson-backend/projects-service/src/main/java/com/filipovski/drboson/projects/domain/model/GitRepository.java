package com.filipovski.drboson.projects.domain.model;

import lombok.Getter;

import javax.persistence.Embeddable;
import java.net.MalformedURLException;
import java.net.URL;

@Embeddable
public class GitRepository {
    private URL repositoryUrl;

    protected GitRepository() { }

    private GitRepository(URL repositoryUrl) {
        this.repositoryUrl = repositoryUrl;
    }

    public static GitRepository from(String url) throws MalformedURLException {
        URL repositoryUrl = new URL(url);

        return new GitRepository(repositoryUrl);
    }

    public URL repositoryURL() throws MalformedURLException {
        return new URL(repositoryUrl.toString());
    }

    @Override
    public String toString() {
        return repositoryUrl.toString();
    }
}
