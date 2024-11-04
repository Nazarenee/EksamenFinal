package dat.daos;

import java.util.List;

public interface IDAO<T, D> {
     T create(T t);
     T delete(D id);
     T read(D id);
     List<T> readAll();
     T update(T t, D d);
}
