package nccc.btp.service;

import java.util.List;
import nccc.btp.entity.ZmmtSupplier;

public interface ZmmtSupplierService {

  List<ZmmtSupplier> findAll();

  ZmmtSupplier add(ZmmtSupplier zmmtSupplier);

  ZmmtSupplier update(ZmmtSupplier zmmtSupplier);

  String delete(String partner);
}
