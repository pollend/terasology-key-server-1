package org.micronaut.sample

import com.datastax.oss.driver.api.core.CqlSession
import io.micronaut.http.HttpResponse
import io.micronaut.http.MutableHttpResponse
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.PathVariable
import javax.inject.Inject

@Controller("/pets")
class PetController(@Inject val session: CqlSession){

    @Get()
    fun index(): MutableHttpResponse<List<Pet>> {
        val results = session.execute("SELECT * from ks.pets")
        val res = results.map {
            Pet(it.getInt("id"), it.getString("name").orEmpty())
        }
        return HttpResponse.ok<List<Pet>>(res.toList())
    }

    @Get("/{id}")
    fun get(@PathVariable id: Int): MutableHttpResponse<Pet> {
        val result = session.execute(session.prepare("SELECT * from ks.pets WHERE id= '?'").bind(id))
        try {
            val row = result.first()
            return HttpResponse.ok<Pet>(Pet(row.getInt("id"), row.getString("name").orEmpty()))
        } catch (ex: NoSuchElementException){
        }
        return HttpResponse.badRequest<Pet>()
    }
}
