package net.guzari.search.elasticsearch.foo;

import net.guzari.search.domain.foo.FooRepository;
import net.guzari.search.domain.foo.Response;
import org.springframework.stereotype.Service;

@Service
public class ElasticSearchFooRepository implements FooRepository {
    @Override
    public Response findFooData(String data) {
        return Response.builder()
                .data("bar")
                .build();
    }
}
