package at.ski.noophaestus.fabric.datagen

import at.ski.noophaestus.datagen.NoophaestusActionTags
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator

object FabricNoophaestusDatagen : DataGeneratorEntrypoint {
    override fun onInitializeDataGenerator(gen: FabricDataGenerator) {
        val pack = gen.createPack()

        pack.addProvider(::NoophaestusActionTags)
    }
}
