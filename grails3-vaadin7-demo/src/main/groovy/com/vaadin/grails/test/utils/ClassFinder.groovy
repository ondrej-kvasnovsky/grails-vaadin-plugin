package com.vaadin.grails.test.utils

import java.util.jar.JarEntry
import java.util.jar.JarFile

class ClassFinder {

    public ArrayList<Class<?>> getClassesForPackage(Package pkg) {
        String pkgname = pkg.getName()
        ArrayList<Class<?>> classes = new ArrayList<Class<?>>()
        // Get a File object for the package
        File directory = null
        String fullPath
        String relPath = pkgname.replace('.', '/')
        System.out.println("ClassDiscovery: Package: " + pkgname + " becomes Path:" + relPath)
        URL resource = ClassLoader.getSystemClassLoader().getResource(relPath)
        System.out.println("ClassDiscovery: Resource = " + resource)
        if (resource == null) {
            throw new RuntimeException("No resource for " + relPath)
        }
        fullPath = resource.getFile()
        System.out.println("ClassDiscovery: FullPath = " + resource)

        try {
            directory = new File(resource.toURI())
        } catch (URISyntaxException e) {
            throw new RuntimeException(pkgname + " (" + resource + ") does not appear to be a valid URL / URI.  Strange, since we got it from the system...", e)
        } catch (IllegalArgumentException e) {
            directory = null
        }
        System.out.println("ClassDiscovery: Directory = " + directory)

        if (directory != null && directory.exists()) {
            // Get the list of the files contained in the package
            String[] files = directory.list()
            for (int i = 0; i < files.length; i++) {
                // we are only interested in .class files
                if (files[i].endsWith(".class")) {
                    // removes the .class extension
                    String className = pkgname + '.' + files[i].substring(0, files[i].length() - 6)
                    System.out.println("ClassDiscovery: className = " + className)
                    try {
                        classes.add(Class.forName(className))
                    }
                    catch (ClassNotFoundException e) {
                        throw new RuntimeException("ClassNotFoundException loading " + className)
                    }
                }
            }
        } else {
            try {
                String jarPath = fullPath.replaceFirst("[.]jar[!].*", ".jar").replaceFirst("file:", "")
                JarFile jarFile = new JarFile(jarPath)
                Enumeration<JarEntry> entries = jarFile.entries()
                while (entries.hasMoreElements()) {
                    JarEntry entry = entries.nextElement()
                    String entryName = entry.getName()
                    if (entryName.startsWith(relPath) && entryName.length() > (relPath.length() + "/".length())) {
                        System.out.println("ClassDiscovery: JarEntry: " + entryName)
                        String className = entryName.replace('/', '.').replace('\\', '.').replace(".class", "")
                        System.out.println("ClassDiscovery: className = " + className)
                        try {
                            classes.add(Class.forName(className))
                        }
                        catch (ClassNotFoundException e) {
                            throw new RuntimeException("ClassNotFoundException loading " + className)
                        }
                    }
                }
            } catch (IOException e) {
                throw new RuntimeException(pkgname + " (" + directory + ") does not appear to be a valid package", e)
            }
        }
        return classes
    }

}
