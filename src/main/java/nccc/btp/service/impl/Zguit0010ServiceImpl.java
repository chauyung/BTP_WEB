package nccc.btp.service.impl;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import nccc.btp.dto.NcccUserDto;
import nccc.btp.entity.Zguit0010;
import nccc.btp.repository.Zguit0010Repository;
import nccc.btp.service.Zguit0010Service;
import nccc.btp.util.SecurityUtil;

@Service
@Transactional
public class Zguit0010ServiceImpl implements Zguit0010Service {

	protected static Logger LOG = LoggerFactory.getLogger(Zguit0010ServiceImpl.class);

	@Autowired
	private Zguit0010Repository Zguit0010Repository;

	@Override
	public List<Zguit0010> findAll() {
		return Zguit0010Repository.findAll(Sort.by(Sort.Direction.ASC, "versSeq"));
	}

	@Override
	public Zguit0010 add(Zguit0010 zguit0010) {
		NcccUserDto user = SecurityUtil.getCurrentUser();
		zguit0010.setUpdateUser(user.getUserId());
		return Zguit0010Repository.save(zguit0010);
	}

	@Override
	public Zguit0010 update(Zguit0010 zguit0010) {
		if (!Zguit0010Repository.existsById(zguit0010.getVersSeq())) {
			throw new RuntimeException(zguit0010.getVersSeq() + " not found");
		}
		NcccUserDto user = SecurityUtil.getCurrentUser();
		zguit0010.setUpdateUser(user.getUserId());
		return Zguit0010Repository.save(zguit0010);
	}

	@Override
	public String delete(String versSeq) {
		if (!Zguit0010Repository.existsById(versSeq)) {
			throw new RuntimeException(versSeq + " not found");
		}
		Zguit0010Repository.deleteById(versSeq);
		return "deleted successfully";
	}
}
