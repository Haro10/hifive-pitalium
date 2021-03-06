/**
 * IT_ALL
 * 結合テスト（screenshot, scrollパッケージ以下）を
 * パラメータで指定した全てのブラウザで実行する。
 */
node {
	stage('Build')
	build(
		job: 'Build',
		parameters: [
			[$class: 'StringParameterValue', name: 'BRANCH_NAME', value: BRANCH_NAME],
			[$class: 'StringParameterValue', name: 'IVY_PROXY_HOST', value: IVY_PROXY_HOST],
			[$class: 'StringParameterValue', name: 'IVY_PROXY_PORT', value: IVY_PROXY_PORT],
			[$class: 'StringParameterValue', name: 'IVY_PROXY_USER', value: IVY_PROXY_USER],
			[$class: 'StringParameterValue', name: 'IVY_PROXY_PASSWORD', value: IVY_PROXY_PASSWORD],
			[$class: 'StringParameterValue', name: 'HUB_HOST', value: HUB_HOST],
			[$class: 'StringParameterValue', name: 'APP_HOST', value: APP_HOST]
		],
		propagate: false
	)
	step(
		$class: 'CopyArtifact',
		projectName: 'Build',
		filter: '**',
		fingerprintArtifacts: true,
		selector: [
			$class: 'StatusBuildSelector',
			stable: false
		]
	)
	step(
		$class: 'ArtifactArchiver',
		artifacts: '**'
	)

	stage('IT (RUN_TEST)')
	parallel(
		IE8: {
			if (IE8 == 'true') {
				buildITJob('IE8')
			}
		},
		IE9: {
			if (IE9 == 'true') {
				buildITJob('IE9')
			}
		},
		IE10: {
			if (IE10 == 'true') {
				buildITJob('IE10')
			}
		},
		IE11_Win7: {
			if (Windows7_IE11 == 'true') {
				buildITJob('IE11_Win7')
			}
		},
		IE11_Win10: {
			if (Windows10_IE11 == 'true') {
				buildITJob('IE11_Win10')
			}
		},
		Edge: {
			if (Edge == 'true') {
				buildITJob('Edge')
			}
		},
		Chrome_Win7: {
			if (Windows7_Chrome == 'true') {
				buildITJob('Chrome_Win7')
			}
		},
		Chrome_Win10: {
			if (Windows10_Chrome == 'true') {
				buildITJob('Chrome_Win10')
			}
		},
		FF_Win7: {
			if (Windows7_Firefox == 'true') {
				buildITJob('FF_Win7')
			}
		},
		FF_Win10: {
			if (Windows10_Firefox == 'true') {
				buildITJob('FF_Win10')
			}
		},
		Chrome_Mac: {
			if (Mac_Chrome == 'true') {
				buildITJob('Chrome_Mac')
			}
		},
		FF_Mac: {
			if (Mac_Firefox == 'true') {
				buildITJob('FF_Mac')
			}
		},
		Safari: {
			if (Safari == 'true') {
				buildITJob('Safari')
			}
		},
		FF_Linux: {
			if (Linux_Firefox == 'true') {
				buildITJob('FF_Linux')
			}
		},
		failFast: false
	)

	def kr = load('pitalium/jenkinsfiles/KillRDP.groovy')
	kr.killAllRDP()
}

/**
 * 指定されたブラウザのITジョブを実行する。
 */
def buildITJob(browserName) {
	build(
		job: "IT_${browserName}",
		parameters: [
			[$class: 'StringParameterValue', name: 'ANT_PROXY_HOST', value: ANT_PROXY_HOST],
			[$class: 'StringParameterValue', name: 'ANT_PROXY_PORT', value: ANT_PROXY_PORT],
			[$class: 'StringParameterValue', name: 'RESULTS_DIR', value: RESULTS_DIR]
		],
		propagate: false
	)
}