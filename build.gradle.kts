plugins {
    `java-library`
}

group = "space.ourmc"
version = "1.0.1"

repositories {
    mavenCentral()
    maven {
        name = "Spigot repository"
        setUrl("https://hub.spigotmc.org/nexus/content/repositories/snapshots")
    }
    maven {
        name = "dmulloy2 repository"
        setUrl("https://repo.dmulloy2.net/nexus/repository/public")
    }
}

dependencies {
    implementation(group="org.bukkit", name="bukkit", version="1.15.2-R0.1-SNAPSHOT")
    implementation(group="com.google.code.gson", name="gson", version="2.8.6")
    implementation(group="com.comphenix.protocol", name="ProtocolLib", version="4.5.0")
    testImplementation(group="junit", name="junit", version="4.12")
}

java.modularity.inferModulePath.set(true)
java.sourceCompatibility = JavaVersion.VERSION_1_8
java.targetCompatibility = JavaVersion.VERSION_1_8

tasks.jar {
    manifest {
        attributes("Implementation-Title" to "oms-user-db-bukkit",
                "Implementation-Version" to archiveVersion,
                "Automatic-Module-Name" to "space.ourmc.userdb.bukkit"
        )
    }
}
