package com.example.github

import org.apache.commons.lang3.StringUtils
import org.gradle.api.DefaultTask
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.TaskAction
import org.kohsuke.github.GitHubBuilder

class GithubSecretsManager implements Plugin<Project> {

	@Override
	void apply(Project project) {
		project.tasks.register('githubSecretsManagerTask', GithubSecretsManagerTask)
	}
}

abstract class GithubSecretsManagerTask extends DefaultTask {
	@TaskAction
	def action() {
		def githubToken = System.getenv("GITHUB_TOKEN")
		if (StringUtils.isEmpty(githubToken)) {
			throw new IllegalStateException("NO github token found :(")
		} else {
			println "GITHUB_TOKEN(${githubToken.length()})"
		}

		def github = new GitHubBuilder().withJwtToken(githubToken).build()
		def pubKey = github.getRepository(System.getenv("GITHUB_REPOSITORY")).getPublicKey()
		println "Repository public key -> ${pubKey.keyId}"
	}
}