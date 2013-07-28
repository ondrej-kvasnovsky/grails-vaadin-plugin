// This script is a Gant script so you can use all special variables
// provided by Gant (such as 'baseDir' which points on project base dir). You can
// use 'ant' to access a global instance of AntBuilder

eventWarStart = { warName ->
}

eventCreateWarStart = { name, stagingDir ->

	// Allow override's in Config.groovy
	boolean removeClientJar = config?.grails?.vaadin?.removeClientJar ?: true
	
	// Remove the vaadin-client jar from the web archive to reduce the size, this jar is only needed for client dev and
	// not in the war carchive, the files size is 15 mb
	if(removeClientJar) 
		ant.delete(dir:"${stagingDir}/WEB-INF/lib/", includes: "vaadin-client-7.*.*.jar", verbose: true)
	
}
