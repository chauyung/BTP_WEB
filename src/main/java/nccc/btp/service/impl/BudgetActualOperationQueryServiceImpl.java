package nccc.btp.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nccc.btp.dto.PreBudgetOperateDto;
import nccc.btp.repository.NcccPreBudgetMRepository;
import nccc.btp.service.BudgetActualOperationQueryService;
import nccc.btp.vo.BudgetVo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class BudgetActualOperationQueryServiceImpl implements BudgetActualOperationQueryService {

    private final NcccPreBudgetMRepository preBudgetRepo;

    @Override
    public List<PreBudgetOperateDto.Row> search(PreBudgetOperateDto.SearchReq req) {
        final String year = trimToNull(req.getYear());
        final String version = trimToNull(req.getVersion());
        final Set<String> opFilter = new HashSet<>(safeList(req.getOperateItemCodes()));

        final List<BudgetVo.BudgetOperateItem> rows =
                preBudgetRepo.GetOperateItemsByYearAndVersion(year, version);

        final List<BudgetVo.BudgetOperateItem> filtered = rows == null ? Collections.emptyList()
                : rows.stream()
                      .filter(r -> opFilter.isEmpty() || opFilter.contains(nz(r.getOperateItemCode())))
                      .collect(Collectors.toList());

        return filtered.stream().map(this::mapToRow).collect(Collectors.toList());
    }

    private PreBudgetOperateDto.Row mapToRow(BudgetVo.BudgetOperateItem r) {
        PreBudgetOperateDto.Row d = new PreBudgetOperateDto.Row();
        d.setYear(nz(r.getYEAR()));
        d.setVersion(nz(r.getVERSION()));
        d.setOuCode(nz(r.getOU_CODE()));
        d.setOuName(nz(r.getOU_NAME()));
        d.setAccounting(nz(r.getACC_CODE()));
        d.setSubject(nz(r.getACC_NAME()));
        d.setBudAmount(nnum(r.getBUD_AMOUNT()));
        d.setOperateItem(nz(r.getOperateItem()));
        d.setOperateItemCode(nz(r.getOperateItemCode()));
        d.setOperateAmt(nnum(r.getOperateAmt()));
        d.setOperateRatio(nnum(r.getOperateRatio()));
        return d;
    }

    // helpers
    private static String nz(String s) { return s == null ? "" : s; }
    private static BigDecimal nnum(BigDecimal x) { return x == null ? BigDecimal.ZERO : x; }

    private static String trimToNull(String s) {
        if (!StringUtils.hasText(s)) return null;
        String t = s.trim();
        return t.isEmpty() ? null : t;
    }

    private static <T> List<T> safeList(List<T> list) {
        return list == null ? Collections.emptyList()
                : list.stream().filter(Objects::nonNull).collect(Collectors.toList());
    }
}