package dev.oblac.tdv.reporter

import com.fasterxml.jackson.databind.json.JsonMapper
import io.github.projectmapk.jackson.module.kogera.KotlinFeature
import io.github.projectmapk.jackson.module.kogera.KotlinModule

val kotlinModule = KotlinModule.Builder()
    .enable(KotlinFeature.StrictNullChecks)
    .build()
val mapper: JsonMapper = JsonMapper.builder()
    .addModule(kotlinModule)
    .build()
