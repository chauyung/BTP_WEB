package nccc.btp.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nccc.btp.dto.BudgetaryPhasePriceingCheckBudgetOpenDto;
import nccc.btp.dto.BudgetaryPhasePriceingCheckBudgetOpenRequest;
import nccc.btp.dto.BudgetaryPhasePriceingDeleteRequest;
import nccc.btp.dto.BudgetaryPhasePriceingDto;
import nccc.btp.dto.BudgetaryPhasePriceingGetBudgetVersionDto;
import nccc.btp.dto.BudgetaryPhasePriceingGetBudgetVersionRequest;
import nccc.btp.dto.BudgetaryPhasePriceingGetItemPriceDetailsRequest;
import nccc.btp.dto.BudgetaryPhasePriceingGetItemPriceDto;
import nccc.btp.dto.BudgetaryPhasePriceingQueryDto;
import nccc.btp.dto.BudgetaryPhasePriceingQueryRequest;
import nccc.btp.dto.BudgetaryPhasePriceingSaveRequest;
import nccc.btp.service.BudgetaryPhasePriceingService;

/**
 * 預算管理模組 : 預算編列-業務目標及分項計價維護(Service)
 * ------------------------------------------------------
 * 建立人員: ChauYung(Team)
 * 建立日期: 2025-10-08
 */
@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class BudgetaryPhasePriceingServiceImpl implements BudgetaryPhasePriceingService{

	@Override
	public List<BudgetaryPhasePriceingQueryDto> query(BudgetaryPhasePriceingQueryRequest req) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void delete(BudgetaryPhasePriceingDeleteRequest req) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void save(BudgetaryPhasePriceingSaveRequest req) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<BudgetaryPhasePriceingGetItemPriceDto> getItemPriceList() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public BudgetaryPhasePriceingGetBudgetVersionDto getBudgetVersion(
			BudgetaryPhasePriceingGetBudgetVersionRequest req) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public BudgetaryPhasePriceingCheckBudgetOpenDto checkBudgetOpen(BudgetaryPhasePriceingCheckBudgetOpenRequest req) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public BudgetaryPhasePriceingDto getItemPriceDetails(BudgetaryPhasePriceingGetItemPriceDetailsRequest req) {
		// TODO Auto-generated method stub
		return null;
	}
}
