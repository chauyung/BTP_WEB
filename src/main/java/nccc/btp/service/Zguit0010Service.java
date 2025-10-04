package nccc.btp.service;

import java.util.List;
import nccc.btp.entity.Zguit0010;

public interface Zguit0010Service {

	List<Zguit0010> findAll();

	Zguit0010 add(Zguit0010 zguit0010);

	Zguit0010 update(Zguit0010 zguit0010);

	String delete(String versSeq);
}
