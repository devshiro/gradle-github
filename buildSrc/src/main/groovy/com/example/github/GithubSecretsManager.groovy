package com.example.github

import com.goterl.lazysodium.LazySodiumJava
import com.goterl.lazysodium.SodiumJava
import com.goterl.lazysodium.utils.Base64MessageEncoder
import com.goterl.lazysodium.utils.Key
import com.goterl.lazysodium.utils.LibraryLoader
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
		project.tasks.register('githubSecretsRead', ReadSecretTask)
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

		def github = new GitHubBuilder().withOAuthToken(githubToken).build()
		def repository = "devshiro/gradle-github"

		def repo = github.getRepository(repository)

		def pubKey = repo.getPublicKey()
		println "Repository public key -> ${pubKey.keyId}"
		def secret = "Nope"

		def sodium = new LazySodiumJava(new SodiumJava(LibraryLoader.Mode.BUNDLED_ONLY), new Base64MessageEncoder())
		def key = Key.fromBase64String(pubKey.key)
		def cypher = sodium.cryptoBoxSealEasy(secret, key)

		println "Cypher -> $cypher"

		repo.createSecret("SUPER_SECRET", cypher, pubKey.keyId)
	}
}

abstract class ReadSecretTask extends DefaultTask {
	@TaskAction
	def action() {
		println "Secret from envvar: ${System.getenv("SUPER_SECRET")} (${System.getenv("SUPER_SECRET").length()})"
	}
}