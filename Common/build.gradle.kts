plugins {
    id("noophaestus.minecraft")
}

architectury {
    common("fabric", "forge")
}

dependencies {
    implementation(libs.kotlin.stdlib)
    implementation(kotlin("reflect"))

    // We depend on fabric loader here to use the fabric @Environment annotations and get the mixin dependencies
    // Do NOT use other classes from fabric loader
    modImplementation(libs.fabric.loader)
    modApi(libs.architectury)

    modApi(libs.hexcasting.common)
    modApi(libs.moreiotas.common) {
        isTransitive = false
    }

    modApi(libs.clothConfig.common) { isTransitive = false }
    modApi(libs.inline.common) {
        isTransitive = false
    }

    modApi(libs.bookshelf.common)


    libs.mixinExtras.common.also {
        implementation(it)
        annotationProcessor(it)
    }
}
