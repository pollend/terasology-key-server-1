package org.micronaut.sample

import com.fasterxml.jackson.annotation.JsonProperty

class Pet(id: Int, name: String) {
    @JsonProperty("name")
    var name: String = name
        get set

    @JsonProperty("id")
    var id: Int = id
        get set

}
