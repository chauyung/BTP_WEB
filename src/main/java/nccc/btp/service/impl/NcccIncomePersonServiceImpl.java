package nccc.btp.service.impl;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import nccc.btp.entity.NcccIncomeTaxDetail;
import nccc.btp.vo.IncomePersonVo;
import nccc.btp.vo.IncomeTaxDetailVo;
import nccc.btp.vo.IncomeTaxU8Vo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import nccc.btp.dto.NcccUserDto;
import nccc.btp.entity.NcccIncomePerson;
import nccc.btp.entity.NcccIncomeTaxParameter;
import nccc.btp.entity.BpmClosedIncomeTax;
import nccc.btp.entity.BpmClosedYear;
import nccc.btp.repository.NcccIncomePersonRepository;
import nccc.btp.repository.NcccIncomeTaxDetailRepository;
import nccc.btp.repository.NcccIncomeTaxParameterRepository;
import nccc.btp.repository.BpmClosedIncomeTaxRepository;
import nccc.btp.repository.BpmClosedYearRepository;
import nccc.btp.service.NcccIncomePersonService;
import nccc.btp.util.SecurityUtil;

@Service
@Transactional
public class NcccIncomePersonServiceImpl implements NcccIncomePersonService {

	protected static Logger LOG = LoggerFactory.getLogger(NcccIncomePersonServiceImpl.class);

	@Autowired
	private NcccIncomePersonRepository ncccIncomePersonRepository;

	@Autowired
	private NcccIncomeTaxDetailRepository ncccIncomeTaxDetailRepository;

	@Autowired
	private NcccIncomeTaxParameterRepository ncccIncomeTaxParameterRepository;

	@Autowired
	private BpmClosedIncomeTaxRepository bpmClosedIncomeTaxRepository;

	@Autowired
	private BpmClosedYearRepository bpmClosedYearRepository;

	// region 所得人主檔

	@Override
	public List<IncomePersonVo> findAll() {
		List<IncomePersonVo> result = new ArrayList<>();
		List<NcccIncomePerson> ncccIncomePersonList = ncccIncomePersonRepository.findAll(Sort.by(Sort.Direction.ASC, "id"));
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		for (NcccIncomePerson data : ncccIncomePersonList) {
			IncomePersonVo vo = new IncomePersonVo();
			vo.setPkId(data.getPkId());
			vo.setId(data.getId());
			vo.setName(data.getName());
			vo.setIncomeCategory(data.getIncomeCategory());
			vo.setCertificateCategory(data.getCertificateCategory());
			vo.setResidence(data.getResidence());
			vo.setErrorNote(data.getErrorNote());
			vo.setPhone1(data.getPhone1());
			vo.setPhone2(data.getPhone2());
			vo.setMobile(data.getMobile());
			vo.setEmail(data.getEmail());
			vo.setContactName(data.getContactName());
			vo.setContactPhone(data.getContactPhone());
			vo.setContactEmail(data.getContactEmail());
			vo.setAddress(data.getAddress());
			vo.setContactAddress(data.getContactAddress());
			vo.setRemark(data.getRemark());
			vo.setSourceFile(data.getSourceFile());
			vo.setUpdateUser(data.getUpdateUser());
			vo.setUpdateDate(data.getUpdateDate().format(formatter));
			result.add(vo);
		}

		return result;
	}

	@Override
	public NcccIncomePerson add(NcccIncomePerson ncccIncomePerson) {
		NcccUserDto user = SecurityUtil.getCurrentUser();
		ncccIncomePerson.setUpdateUser(user.getUserId());
		return ncccIncomePersonRepository.save(ncccIncomePerson);
	}

	@Override
	public NcccIncomePerson update(NcccIncomePerson ncccIncomePerson) {
		if (!ncccIncomePersonRepository.existsById(ncccIncomePerson.getPkId())) {
			throw new RuntimeException(ncccIncomePerson.getId() + " not found");
		}
		NcccUserDto user = SecurityUtil.getCurrentUser();
		ncccIncomePerson.setUpdateUser(user.getUserId());
		return ncccIncomePersonRepository.save(ncccIncomePerson);
	}

	@Override
	public String delete(Long pkId) {
		if (!ncccIncomePersonRepository.existsById(pkId)) {
			throw new RuntimeException(pkId + " not found");
		}
		ncccIncomePersonRepository.deleteById(pkId);
		return pkId + " deleted successfully";
	}

	/**
	 * 取得所得人資料
	 * @param id
	 * @param name
	 * @return
	 */
	public IncomePersonVo GetIncomePersonByIdAndName(String id, String name)
	{
		IncomePersonVo result = new IncomePersonVo();

		if(id != null && name != null)
		{
			NcccIncomePerson ncccIncomePerson = null;

			if(name.isEmpty())
			{
				ncccIncomePerson = ncccIncomePersonRepository.findById(id);
			}
			else
			{
				ncccIncomePerson = ncccIncomePersonRepository.findByIdAndName(id,name);
			}

			if(ncccIncomePerson != null)
			{
				result.setId(ncccIncomePerson.getId());
				result.setName(ncccIncomePerson.getName());
				result.setIncomeCategory(ncccIncomePerson.getIncomeCategory());
				result.setCertificateCategory(ncccIncomePerson.getCertificateCategory());
				result.setResidence(ncccIncomePerson.getResidence());
				result.setErrorNote(ncccIncomePerson.getErrorNote());
				result.setContactName(ncccIncomePerson.getContactName());
				result.setContactPhone(ncccIncomePerson.getContactPhone());
				result.setAddress(ncccIncomePerson.getAddress());
				result.setContactAddress(ncccIncomePerson.getContactAddress());
			}
		}

		return result;
	}

	// endregion

	// region 申報資料

	// 取得年度所得稅申報資料
	public List<IncomeTaxDetailVo> getIncomeTaxDetailListByPaymentYear(String paymentYear)
	{
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

		List<IncomeTaxDetailVo> result = new  ArrayList<>();

		List<NcccIncomeTaxDetail> ncccIncomeTaxDetailList = ncccIncomeTaxDetailRepository.findByPaymentYear(paymentYear);

		List<NcccIncomePerson> ncccIncomePersonList = ncccIncomePersonRepository.findAll();

		for(NcccIncomeTaxDetail detailData : ncccIncomeTaxDetailList)
		{
			Optional<NcccIncomePerson> foundIP = ncccIncomePersonList.stream().filter(o-> o.getPkId().equals(detailData.getPkMId()))
					.findFirst();

			if(foundIP.isPresent())
			{
				NcccIncomePerson ncccIncomePerson = foundIP.get();

				IncomeTaxDetailVo detailVo = new IncomeTaxDetailVo();
				detailVo.setId(detailData.getId());
				detailVo.setPkMId(detailData.getPkMId());
				detailVo.setBan(ncccIncomePerson.getId());
				detailVo.setName(ncccIncomePerson.getName());
				detailVo.setPaymentYear(detailData.getPaymentYear());
				detailVo.setPaymentMonthSt(detailData.getPaymentMonthSt());
				detailVo.setPaymentMonthEd(detailData.getPaymentMonthEd());
				detailVo.setSoftwareNote(detailData.getSoftwareNote());
				detailVo.setIncomeType(detailData.getIncomeType());
				detailVo.setIncomeNote(detailData.getIncomeNote());
				detailVo.setGrossPayment(detailData.getGrossPayment());
				detailVo.setWithholdingTaxRate(detailData.getWithholdingTaxRate());
				detailVo.setWithholdingTax(detailData.getWithholdingTax());
				detailVo.setNhRate(detailData.getNhRate());
				detailVo.setNhWithHolding(detailData.getNhWithHolding());
				detailVo.setNetPayment(detailData.getNetPayment());
				detailVo.setPaymentDate(detailData.getPaymentDate().format(formatter));
				detailVo.setTaxTreatyCode(detailData.getTaxTreatyCode());
				detailVo.setTaxCreditFlag(detailData.getTaxCreditFlag());
				detailVo.setCertificateIssueMethod(detailData.getCertificateIssueMethod());
				detailVo.setTaxIdentificationNo(detailData.getTaxIdentificationNo());
				detailVo.setCountryCode(detailData.getCountryCode());
				detailVo.setHas183Days(detailData.getHas183Days());
				detailVo.setShareColumn1(detailData.getShareColumn1());
				detailVo.setShareColumn2(detailData.getShareColumn2());
				detailVo.setShareColumn3(detailData.getShareColumn3());
				detailVo.setShareColumn4(detailData.getShareColumn4());
				detailVo.setShareColumn5(detailData.getShareColumn5());
				detailVo.setRemark(detailData.getRemark());
				detailVo.setBtpOrderNo(detailData.getBtpOrderNo());
				detailVo.setSapDocNo(detailData.getSapDocNo());
				detailVo.setSourceFile(detailData.getSourceFile());
				result.add(detailVo);
			}
		}

		return result;
	}

	// 取得所得稅申報資料
	public List<IncomeTaxDetailVo> getIncomeTaxDetailList(long pkId)
	{
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

		List<IncomeTaxDetailVo> result = new  ArrayList<>();

		List<NcccIncomeTaxDetail> ncccIncomeTaxDetailList = ncccIncomeTaxDetailRepository.findBypkMId(pkId);

		for(NcccIncomeTaxDetail detailData : ncccIncomeTaxDetailList)
		{
			IncomeTaxDetailVo detailVo = new IncomeTaxDetailVo();
			detailVo.setId(detailData.getId());
			detailVo.setPkMId(detailData.getPkMId());
			detailVo.setPaymentYear(detailData.getPaymentYear());
			detailVo.setPaymentMonthSt(detailData.getPaymentMonthSt());
			detailVo.setPaymentMonthEd(detailData.getPaymentMonthEd());
			detailVo.setSoftwareNote(detailData.getSoftwareNote());
			detailVo.setIncomeType(detailData.getIncomeType());
			detailVo.setIncomeNote(detailData.getIncomeNote());
			detailVo.setGrossPayment(detailData.getGrossPayment());
			detailVo.setWithholdingTaxRate(detailData.getWithholdingTaxRate());
			detailVo.setWithholdingTax(detailData.getWithholdingTax());
			detailVo.setNhRate(detailData.getNhRate());
			detailVo.setNhWithHolding(detailData.getNhWithHolding());
			detailVo.setNetPayment(detailData.getNetPayment());
			detailVo.setPaymentDate(detailData.getPaymentDate().format(formatter));
			detailVo.setTaxTreatyCode(detailData.getTaxTreatyCode());
			detailVo.setTaxCreditFlag(detailData.getTaxCreditFlag());
			detailVo.setCertificateIssueMethod(detailData.getCertificateIssueMethod());
			detailVo.setTaxIdentificationNo(detailData.getTaxIdentificationNo());
			detailVo.setCountryCode(detailData.getCountryCode());
			detailVo.setHas183Days(detailData.getHas183Days());
			detailVo.setShareColumn1(detailData.getShareColumn1());
			detailVo.setShareColumn2(detailData.getShareColumn2());
			detailVo.setShareColumn3(detailData.getShareColumn3());
			detailVo.setShareColumn4(detailData.getShareColumn4());
			detailVo.setShareColumn5(detailData.getShareColumn5());
			detailVo.setRemark(detailData.getRemark());
			detailVo.setBtpOrderNo(detailData.getBtpOrderNo());
			detailVo.setSapDocNo(detailData.getSapDocNo());
			detailVo.setSourceFile(detailData.getSourceFile());
			result.add(detailVo);
		}

		return result;

	}

	// 新增申報資料
	public NcccIncomeTaxDetail addTaxDetail(IncomeTaxDetailVo vo)
	{
		NcccIncomeTaxDetail data = new NcccIncomeTaxDetail();
		data.setPkMId(vo.getPkMId());
		data.setPaymentYear(vo.getPaymentYear());
		data.setPaymentMonthSt(vo.getPaymentMonthSt());
		data.setPaymentMonthEd(vo.getPaymentMonthEd());
		data.setSoftwareNote(vo.getSoftwareNote());
		data.setIncomeType(vo.getIncomeType());
		data.setIncomeNote(vo.getIncomeNote());
		data.setGrossPayment(vo.getGrossPayment());
		data.setWithholdingTaxRate(vo.getWithholdingTaxRate());
		data.setWithholdingTax(vo.getWithholdingTax());
		data.setNhRate(vo.getNhRate());
		data.setNhWithHolding(vo.getNhWithHolding());
		data.setNetPayment(vo.getNetPayment());
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		if(vo.getPaymentDate() != null && !vo.getPaymentDate().isEmpty())
		{
			data.setPaymentDate(LocalDate.parse(vo.getPaymentDate(), formatter));
		}
		data.setTaxTreatyCode(vo.getTaxTreatyCode());
		data.setTaxCreditFlag(vo.getTaxCreditFlag());
		data.setCertificateIssueMethod(vo.getCertificateIssueMethod());
		data.setTaxIdentificationNo(vo.getTaxIdentificationNo());
		data.setCountryCode(vo.getCountryCode());
		data.setHas183Days(vo.getHas183Days());
		data.setShareColumn1(vo.getShareColumn1());
		data.setShareColumn2(vo.getShareColumn2());
		data.setShareColumn3(vo.getShareColumn3());
		data.setShareColumn4(vo.getShareColumn4());
		data.setShareColumn5(vo.getShareColumn5());
		data.setRemark(vo.getRemark());
		data.setBtpOrderNo("");
		data.setSapDocNo("");
		data.setSourceFile(vo.getSourceFile());
		return ncccIncomeTaxDetailRepository.save(data);
	}

	// 編輯申報資料
	public NcccIncomeTaxDetail updateTaxDetail(IncomeTaxDetailVo vo)
	{
		if (!ncccIncomeTaxDetailRepository.existsById(vo.getId())) {
			throw new RuntimeException(vo.getId() + " not found");
		}
		NcccIncomeTaxDetail data = ncccIncomeTaxDetailRepository.findById(vo.getId()).get();
		data.setPaymentYear(vo.getPaymentYear());
		data.setPaymentMonthSt(vo.getPaymentMonthSt());
		data.setPaymentMonthEd(vo.getPaymentMonthEd());
		data.setSoftwareNote(vo.getSoftwareNote());
		data.setIncomeType(vo.getIncomeType());
		data.setIncomeNote(vo.getIncomeNote());
		data.setGrossPayment(vo.getGrossPayment());
		data.setWithholdingTaxRate(vo.getWithholdingTaxRate());
		data.setWithholdingTax(vo.getWithholdingTax());
		data.setNhRate(vo.getNhRate());
		data.setNhWithHolding(vo.getNhWithHolding());
		data.setNetPayment(vo.getNetPayment());
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		if(vo.getPaymentDate() != null && !vo.getPaymentDate().isEmpty())
		{
			data.setPaymentDate(LocalDate.parse(vo.getPaymentDate(), formatter));
		}
		data.setTaxTreatyCode(vo.getTaxTreatyCode());
		data.setTaxCreditFlag(vo.getTaxCreditFlag());
		data.setCertificateIssueMethod(vo.getCertificateIssueMethod());
		data.setTaxIdentificationNo(vo.getTaxIdentificationNo());
		data.setCountryCode(vo.getCountryCode());
		data.setHas183Days(vo.getHas183Days());
		data.setShareColumn1(vo.getShareColumn1());
		data.setShareColumn2(vo.getShareColumn2());
		data.setShareColumn3(vo.getShareColumn3());
		data.setShareColumn4(vo.getShareColumn4());
		data.setShareColumn5(vo.getShareColumn5());
		data.setRemark(vo.getRemark());
		data.setSourceFile(vo.getSourceFile());
		return ncccIncomeTaxDetailRepository.save(data);
	}

	// 刪除申報資料
	public String deleteTaxDetail(Long id) {
		if (!ncccIncomeTaxDetailRepository.existsById(id)) {
			throw new RuntimeException(id + " not found");
		}
		ncccIncomePersonRepository.deleteById(id);
		return "deleted successfully";
	}

	// endregion

	// region 所得稅參數

	//取得所得稅參數
	public NcccIncomeTaxParameter getIncomeTaxParameter()
	{
		return ncccIncomeTaxParameterRepository.findFirstByOrderByReportingYearDesc();
	}

	// endregion

	// region 所得申報資料產生

	//取得年份所得申報u8資料
	public List<IncomeTaxU8Vo> getChooseYearIncomeTaxU8Data(String year,Integer startIndex)
	{
		List<IncomeTaxU8Vo> result = new ArrayList<>();

		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

		//判定是否有關帳資料
		BpmClosedYear bpmClosedYear = bpmClosedYearRepository.findByYear(year);

		//已關帳,不需要重新產生單號
		if(bpmClosedYear.getClosed().equals("Y"))
		{
			List<BpmClosedIncomeTax> bpmClosedIncomeTaxList = bpmClosedIncomeTaxRepository.findByPaymentYear(year);

			for(BpmClosedIncomeTax  bpmClosedIncomeTax : bpmClosedIncomeTaxList)
			{
				IncomeTaxU8Vo newIncomeTaxU8Vo = new IncomeTaxU8Vo();
				newIncomeTaxU8Vo.setPaymentYear(bpmClosedIncomeTax.getPaymentYear());
				newIncomeTaxU8Vo.setAgency(bpmClosedIncomeTax.getAgency());
				newIncomeTaxU8Vo.setOrderNo(bpmClosedIncomeTax.getOrderNo());
				newIncomeTaxU8Vo.setPaymentMonthSt(bpmClosedIncomeTax.getPaymentMonthSt());
				newIncomeTaxU8Vo.setPaymentMonthEd(bpmClosedIncomeTax.getPaymentMonthEd());
				newIncomeTaxU8Vo.setBan(bpmClosedIncomeTax.getBan());
				newIncomeTaxU8Vo.setName(bpmClosedIncomeTax.getName());
				newIncomeTaxU8Vo.setAddress(bpmClosedIncomeTax.getAddress());
				newIncomeTaxU8Vo.setCertificateCategory(bpmClosedIncomeTax.getCertificateCategory());
				newIncomeTaxU8Vo.setErrorNote(bpmClosedIncomeTax.getErrorNote());
				newIncomeTaxU8Vo.setSoftwareNote(bpmClosedIncomeTax.getSoftwareNote());
				newIncomeTaxU8Vo.setIncomeNote(bpmClosedIncomeTax.getIncomeNote());
				newIncomeTaxU8Vo.setIncomeType(bpmClosedIncomeTax.getIncomeType());
				newIncomeTaxU8Vo.setGrossPayment(bpmClosedIncomeTax.getGrossPayment());
				newIncomeTaxU8Vo.setWithholdingTaxRate(bpmClosedIncomeTax.getWithholdingTaxRate());
				newIncomeTaxU8Vo.setWithholdingTax(bpmClosedIncomeTax.getWithholdingTax());
				newIncomeTaxU8Vo.setNetPayment(bpmClosedIncomeTax.getNetPayment());
				newIncomeTaxU8Vo.setPaymentDate(bpmClosedIncomeTax.getPaymentDate());
				newIncomeTaxU8Vo.setBtpOrderNo(bpmClosedIncomeTax.getBtpOrderNo());
				newIncomeTaxU8Vo.setSapDocNo(bpmClosedIncomeTax.getSapDocNo());
				newIncomeTaxU8Vo.setShareColumn1(bpmClosedIncomeTax.getShareColumn1());
				newIncomeTaxU8Vo.setShareColumn2(bpmClosedIncomeTax.getShareColumn2());
				newIncomeTaxU8Vo.setShareColumn3(bpmClosedIncomeTax.getShareColumn3());
				newIncomeTaxU8Vo.setShareColumn4(bpmClosedIncomeTax.getShareColumn4());
				newIncomeTaxU8Vo.setShareColumn5(bpmClosedIncomeTax.getShareColumn5());
				newIncomeTaxU8Vo.setResidence(bpmClosedIncomeTax.getResidence());
				newIncomeTaxU8Vo.setTaxCreditFlag(bpmClosedIncomeTax.getTaxCreditFlag());
				newIncomeTaxU8Vo.setCertificateIssueMethod(bpmClosedIncomeTax.getCertificateIssueMethod());
				newIncomeTaxU8Vo.setHas183Days(bpmClosedIncomeTax.getHas183Days());
				newIncomeTaxU8Vo.setCountryCode(bpmClosedIncomeTax.getCountryCode());
				newIncomeTaxU8Vo.setTaxTreatyCode(bpmClosedIncomeTax.getTaxTreatyCode());
				newIncomeTaxU8Vo.setTaxIdentificationNo(bpmClosedIncomeTax.getTaxIdentificationNo());
				result.add(newIncomeTaxU8Vo);
			}
		}
		else {

			List<NcccIncomePerson> incomePersonList = ncccIncomePersonRepository.findAll();

			List<NcccIncomeTaxDetail> ncccIncomeTaxDetailList = ncccIncomeTaxDetailRepository.findByPaymentYear(year);

			NcccIncomeTaxParameter ncccIncomeTaxParameter = ncccIncomeTaxParameterRepository.findFirstByOrderByReportingYearDesc();

			String Agency = "";

			if(ncccIncomeTaxParameter != null)
			{
				Agency = ncccIncomeTaxParameter.getAgency();
			}

			for(NcccIncomeTaxDetail  ncccIncomeTaxDetail : ncccIncomeTaxDetailList)
			{
				Optional<NcccIncomePerson> foundIP = incomePersonList.stream().filter(o-> o.getPkId().equals(ncccIncomeTaxDetail.getPkMId()))
						.findFirst();

				if(foundIP.isPresent())
				{
					NcccIncomePerson ncccIncomePerson = foundIP.get();

					Optional<IncomeTaxU8Vo> foundU8 = result.stream().filter(o-> !o.getBan().equals(ncccIncomePerson.getId()) && o.getIncomeNote().equals(ncccIncomeTaxDetail.getIncomeNote()) && o.getIncomeType().equals(ncccIncomeTaxDetail.getIncomeType())).findFirst();

					if(foundU8.isPresent())
					{
						IncomeTaxU8Vo incomeTaxU8Vo = foundU8.get();
						incomeTaxU8Vo.setSoftwareNote(ncccIncomeTaxDetail.getSoftwareNote());
						incomeTaxU8Vo.setIncomeNote(ncccIncomeTaxDetail.getIncomeNote());
						incomeTaxU8Vo.setIncomeType(ncccIncomeTaxDetail.getIncomeType());
						incomeTaxU8Vo.setGrossPayment(incomeTaxU8Vo.getGrossPayment().add(ncccIncomeTaxDetail.getGrossPayment()));
						incomeTaxU8Vo.setWithholdingTaxRate(ncccIncomeTaxDetail.getWithholdingTaxRate());
						incomeTaxU8Vo.setWithholdingTax(incomeTaxU8Vo.getWithholdingTax().add(ncccIncomeTaxDetail.getWithholdingTax()));
						incomeTaxU8Vo.setNetPayment(incomeTaxU8Vo.getNetPayment().add(ncccIncomeTaxDetail.getGrossPayment().subtract(ncccIncomeTaxDetail.getWithholdingTax())));
						incomeTaxU8Vo.setPaymentDate(ncccIncomeTaxDetail.getPaymentDate().format(formatter));
						incomeTaxU8Vo.setBtpOrderNo(ncccIncomeTaxDetail.getBtpOrderNo());
						incomeTaxU8Vo.setSapDocNo(ncccIncomeTaxDetail.getSapDocNo());
						incomeTaxU8Vo.setShareColumn1(ncccIncomeTaxDetail.getShareColumn1());
						incomeTaxU8Vo.setShareColumn2(ncccIncomeTaxDetail.getShareColumn2());
						incomeTaxU8Vo.setShareColumn3(ncccIncomeTaxDetail.getShareColumn3());
						incomeTaxU8Vo.setShareColumn4(ncccIncomeTaxDetail.getShareColumn4());
						incomeTaxU8Vo.setShareColumn5(ncccIncomeTaxDetail.getShareColumn5());
						incomeTaxU8Vo.setResidence(ncccIncomePerson.getResidence());
						incomeTaxU8Vo.setTaxCreditFlag(ncccIncomeTaxDetail.getTaxCreditFlag());
						incomeTaxU8Vo.setCertificateIssueMethod(ncccIncomeTaxDetail.getCertificateIssueMethod());
						incomeTaxU8Vo.setHas183Days(ncccIncomeTaxDetail.getHas183Days());
						incomeTaxU8Vo.setCountryCode(ncccIncomeTaxDetail.getCountryCode());
						incomeTaxU8Vo.setTaxTreatyCode(ncccIncomeTaxDetail.getTaxTreatyCode());
						incomeTaxU8Vo.setTaxIdentificationNo(ncccIncomeTaxDetail.getTaxIdentificationNo());

					}
					else
					{
						IncomeTaxU8Vo newIncomeTaxU8Vo = new IncomeTaxU8Vo();
						newIncomeTaxU8Vo.setPaymentYear(ncccIncomeTaxDetail.getPaymentYear());
						newIncomeTaxU8Vo.setAgency(Agency);
						String serialStr = String.format("%08d", startIndex);
						newIncomeTaxU8Vo.setOrderNo(serialStr);
						newIncomeTaxU8Vo.setPaymentMonthSt("01");
						newIncomeTaxU8Vo.setPaymentMonthEd("12");
						newIncomeTaxU8Vo.setBan(ncccIncomePerson.getId());
						newIncomeTaxU8Vo.setName(ncccIncomePerson.getName());
						newIncomeTaxU8Vo.setAddress(ncccIncomePerson.getAddress());
						newIncomeTaxU8Vo.setCertificateCategory(ncccIncomePerson.getCertificateCategory());
						newIncomeTaxU8Vo.setErrorNote(ncccIncomePerson.getErrorNote());
						newIncomeTaxU8Vo.setSoftwareNote(ncccIncomeTaxDetail.getSoftwareNote());
						newIncomeTaxU8Vo.setIncomeNote(ncccIncomeTaxDetail.getIncomeNote());
						newIncomeTaxU8Vo.setIncomeType(ncccIncomeTaxDetail.getIncomeType());
						newIncomeTaxU8Vo.setGrossPayment(ncccIncomeTaxDetail.getGrossPayment());
						newIncomeTaxU8Vo.setWithholdingTaxRate(ncccIncomeTaxDetail.getWithholdingTaxRate());
						newIncomeTaxU8Vo.setWithholdingTax(ncccIncomeTaxDetail.getWithholdingTax());
						newIncomeTaxU8Vo.setNetPayment(ncccIncomeTaxDetail.getGrossPayment().subtract(ncccIncomeTaxDetail.getWithholdingTax()));
						newIncomeTaxU8Vo.setPaymentDate(ncccIncomeTaxDetail.getPaymentDate().format(formatter));
						newIncomeTaxU8Vo.setBtpOrderNo(ncccIncomeTaxDetail.getBtpOrderNo());
						newIncomeTaxU8Vo.setSapDocNo(ncccIncomeTaxDetail.getSapDocNo());
						newIncomeTaxU8Vo.setShareColumn1(ncccIncomeTaxDetail.getShareColumn1());
						newIncomeTaxU8Vo.setShareColumn2(ncccIncomeTaxDetail.getShareColumn2());
						newIncomeTaxU8Vo.setShareColumn3(ncccIncomeTaxDetail.getShareColumn3());
						newIncomeTaxU8Vo.setShareColumn4(ncccIncomeTaxDetail.getShareColumn4());
						newIncomeTaxU8Vo.setShareColumn5(ncccIncomeTaxDetail.getShareColumn5());
						newIncomeTaxU8Vo.setResidence(ncccIncomePerson.getResidence());
						newIncomeTaxU8Vo.setTaxCreditFlag(ncccIncomeTaxDetail.getTaxCreditFlag());
						newIncomeTaxU8Vo.setCertificateIssueMethod(ncccIncomeTaxDetail.getCertificateIssueMethod());
						newIncomeTaxU8Vo.setHas183Days(ncccIncomeTaxDetail.getHas183Days());
						newIncomeTaxU8Vo.setCountryCode(ncccIncomeTaxDetail.getCountryCode());
						newIncomeTaxU8Vo.setTaxTreatyCode(ncccIncomeTaxDetail.getTaxTreatyCode());
						newIncomeTaxU8Vo.setTaxIdentificationNo(ncccIncomeTaxDetail.getTaxIdentificationNo());
						result.add(newIncomeTaxU8Vo);
						startIndex++;
					}
				}
			}

			// region 覆蓋新資料

			//刪除舊資料
			bpmClosedIncomeTaxRepository.deleteByPaymentYear(year);

			List<BpmClosedIncomeTax> bpmClosedIncomeTaxList = new ArrayList<>();

			for(IncomeTaxU8Vo incomeTaxU8Vo : result)
			{
				BpmClosedIncomeTax bpmClosedIncomeTax = new BpmClosedIncomeTax();
				bpmClosedIncomeTax.setPaymentYear(incomeTaxU8Vo.getPaymentYear());
				bpmClosedIncomeTax.setAgency(incomeTaxU8Vo.getAgency());
				bpmClosedIncomeTax.setOrderNo(incomeTaxU8Vo.getOrderNo());
				bpmClosedIncomeTax.setPaymentMonthSt(incomeTaxU8Vo.getPaymentMonthSt());
				bpmClosedIncomeTax.setPaymentMonthEd(incomeTaxU8Vo.getPaymentMonthEd());
				bpmClosedIncomeTax.setBan(incomeTaxU8Vo.getBan());
				bpmClosedIncomeTax.setName(incomeTaxU8Vo.getName());
				bpmClosedIncomeTax.setAddress(incomeTaxU8Vo.getAddress());
				bpmClosedIncomeTax.setCertificateCategory(incomeTaxU8Vo.getCertificateCategory());
				bpmClosedIncomeTax.setErrorNote(incomeTaxU8Vo.getErrorNote());
				bpmClosedIncomeTax.setSoftwareNote(incomeTaxU8Vo.getSoftwareNote());
				bpmClosedIncomeTax.setIncomeNote(incomeTaxU8Vo.getIncomeNote());
				bpmClosedIncomeTax.setIncomeType(incomeTaxU8Vo.getIncomeType());
				bpmClosedIncomeTax.setGrossPayment(incomeTaxU8Vo.getGrossPayment());
				bpmClosedIncomeTax.setWithholdingTaxRate(incomeTaxU8Vo.getWithholdingTaxRate());
				bpmClosedIncomeTax.setWithholdingTax(incomeTaxU8Vo.getWithholdingTax());
				bpmClosedIncomeTax.setNetPayment(incomeTaxU8Vo.getNetPayment());
				bpmClosedIncomeTax.setPaymentDate(incomeTaxU8Vo.getPaymentDate());
				bpmClosedIncomeTax.setBtpOrderNo(incomeTaxU8Vo.getBtpOrderNo());
				bpmClosedIncomeTax.setSapDocNo(incomeTaxU8Vo.getSapDocNo());
				bpmClosedIncomeTax.setShareColumn1(incomeTaxU8Vo.getShareColumn1());
				bpmClosedIncomeTax.setShareColumn2(incomeTaxU8Vo.getShareColumn2());
				bpmClosedIncomeTax.setShareColumn3(incomeTaxU8Vo.getShareColumn3());
				bpmClosedIncomeTax.setShareColumn4(incomeTaxU8Vo.getShareColumn4());
				bpmClosedIncomeTax.setShareColumn5(incomeTaxU8Vo.getShareColumn5());
				bpmClosedIncomeTax.setResidence(incomeTaxU8Vo.getResidence());
				bpmClosedIncomeTax.setTaxCreditFlag(incomeTaxU8Vo.getTaxCreditFlag());
				bpmClosedIncomeTax.setCertificateIssueMethod(incomeTaxU8Vo.getCertificateIssueMethod());
				bpmClosedIncomeTax.setHas183Days(incomeTaxU8Vo.getHas183Days());
				bpmClosedIncomeTax.setCountryCode(incomeTaxU8Vo.getCountryCode());
				bpmClosedIncomeTax.setTaxTreatyCode(incomeTaxU8Vo.getTaxTreatyCode());
				bpmClosedIncomeTax.setTaxIdentificationNo(incomeTaxU8Vo.getTaxIdentificationNo());
				bpmClosedIncomeTaxList.add(bpmClosedIncomeTax);
			}

			// endregion
		}
		return result;
	}

	// endregion


}
