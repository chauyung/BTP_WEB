package nccc.btp.service;

import java.util.List;
import nccc.btp.entity.NcccIncomePerson;
import nccc.btp.entity.NcccIncomeTaxDetail;
import nccc.btp.entity.NcccIncomeTaxParameter;
import nccc.btp.vo.IncomePersonVo;
import nccc.btp.vo.IncomeTaxDetailVo;

public interface NcccIncomePersonService {

	List<IncomePersonVo> findAll();

	// 取得所得稅申報資料
	List<IncomeTaxDetailVo> getIncomeTaxDetailList(long pkId);

	// 取得年度所得稅申報資料
	List<IncomeTaxDetailVo> getIncomeTaxDetailListByPaymentYear(String paymentYear);

	//取得所得稅參數
	NcccIncomeTaxParameter getIncomeTaxParameter();

	NcccIncomePerson add(NcccIncomePerson ncccIncomePerson);

	NcccIncomePerson update(NcccIncomePerson ncccIncomePerson);

	String delete(Long id);

	// 新增申報資料
	NcccIncomeTaxDetail addTaxDetail(IncomeTaxDetailVo vo);

	// 編輯申報資料
	NcccIncomeTaxDetail updateTaxDetail(IncomeTaxDetailVo vo);

	// 刪除申報資料
	String deleteTaxDetail(Long id);

	/**
	 * 取得所得人資料
	 * @param id
	 * @param name
	 * @return
	 */
	IncomePersonVo GetIncomePersonByIdAndName(String id, String name);
}
