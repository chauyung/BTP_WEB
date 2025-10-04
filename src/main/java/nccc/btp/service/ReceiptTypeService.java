package nccc.btp.service;

import java.util.List;
import nccc.btp.entity.NcccReceiptType;

public interface ReceiptTypeService {

  List<NcccReceiptType> findAll();

  NcccReceiptType add(NcccReceiptType ncccReceiptType);

  NcccReceiptType update(NcccReceiptType ncccReceiptType);

  String delete(Long id);
}
