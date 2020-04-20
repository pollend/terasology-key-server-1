package org.micronaut.sample

import com.datastax.oss.driver.api.core.AllNodesFailedException
import com.datastax.oss.driver.api.core.CqlSession
import com.datastax.oss.driver.internal.core.retry.DefaultRetryPolicy
import io.micronaut.configuration.cassandra.CassandraSessionFactory
import io.micronaut.context.event.ApplicationEventListener
import io.micronaut.discovery.event.ServiceStartedEvent
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import javax.inject.Singleton


@Singleton
class ApplicationStartup(val session: CqlSession) : ApplicationEventListener<ServiceStartedEvent>{
    private val LOG: Logger = LoggerFactory.getLogger(ApplicationStartup::class.java)
    override fun onApplicationEvent(event: ServiceStartedEvent?) {
        session.execute("CREATE KEYSPACE IF NOT EXISTS ks WITH replication = {'class':'SimpleStrategy', 'replication_factor':1};")
        val results = session.execute("SELECT table_name FROM system_schema.tables WHERE keyspace_name='ks';")
        var flag = false;
        results.forEach {
            if (it.getString("table_name").equals("pets")) {
                flag = true;
            }
        }
        if (flag == false) {
            session.execute("CREATE TABLE IF NOT EXISTS ks.pets(id int PRIMARY KEY, name text)")
            session.execute("Insert into ks.pets(id,name) values (1,'husky')")
            session.execute("Insert into ks.pets(id,name) values (2,'labrador')")
            session.execute("Insert into ks.pets(id,name) values (3,'pomeranian')")
            session.execute("Insert into ks.pets(id,name) values (4,'pug')")
            session.execute("Insert into ks.pets(id,name) values (5,'poodle')")
        }
    }
}
