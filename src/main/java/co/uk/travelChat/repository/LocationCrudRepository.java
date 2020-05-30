package co.uk.travelChat.repository;

import co.uk.travelChat.model.Location;
import org.springframework.data.couchbase.core.query.Query;
import org.springframework.data.couchbase.repository.ReactiveCouchbaseSortingRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface LocationCrudRepository extends ReactiveCouchbaseSortingRepository<Location, String> {

//      SELECT * FROM TravelChat USE KEYS ["London Victoria"];
//      SELECT META().id FROM TravelChat WHERE META().id LIKE "%London Victoria%"
//      SELECT meta(TravelChat).id FROM `TravelChat` WHERE latitude = -0.143897 LIMIT 1;
//      SELECT meta(TravelChat).id, TravelChat.latitude, TravelChat.longitude FROM `TravelChat` WHERE latitude = -0.143897 AND longitude = 51.495213 LIMIT 1;


    @Query("#{#n1ql.selectEntity} where #{#n1ql.filter} ORDER BY name LIMIT $1 OFFSET $2;")
    Flux<Location> findPage(int limit, int offset);


    @Query("#{#n1ql.selectEntity} where #{#n1ql.filter} AND name LIKE $1 ORDER BY name LIMIT 5 OFFSET 0;")
//todo fix
    Flux<Location> findLike(String name);//remove

//    Flux<Location> findLocationsByNameAndLongitudeAndLatitude(String name, double longitude, double latitude);//remove

    //    @Query("#{#n1ql.selectEntity} where #{#n1ql.filter} and companyId = $1 and $2 within #{#n1ql.bucket}")
    @Query("#{#n1ql.selectEntity} where #{#n1ql.filter} AND longitude = $1 AND latitude = $2 LIMIT 1;")
    Flux<Location> getByLongAndLat(double longitude, double latitude);

//    Flux<Location> findLocationsByName(String name);//remove
}