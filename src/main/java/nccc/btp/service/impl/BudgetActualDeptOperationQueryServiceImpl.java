package nccc.btp.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nccc.btp.dto.BudgetActualDeptOperationQueryRequest;
import nccc.btp.entity.NcccBudgetActual;
import nccc.btp.repository.BudgetActualRepository;
import nccc.btp.service.BudgetActualDeptOperationQueryService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class BudgetActualDeptOperationQueryServiceImpl implements BudgetActualDeptOperationQueryService {

    private final BudgetActualRepository repository;

    @Override
    @Transactional(readOnly = true)
    public List<NcccBudgetActual> search(BudgetActualDeptOperationQueryRequest req) {

        String yymm = StringUtils.hasText(req.getYymm())
                ? req.getYymm().trim() : null;

        String ver = StringUtils.hasText(req.getVersion())
                ? req.getVersion().trim() : null;

        
        List<String> opList = null;
        Integer opEmpty = 1;
        if (req.getOperateItemCodes() != null && !req.getOperateItemCodes().isEmpty()) {
            opList = req.getOperateItemCodes().stream()
                    .filter(Objects::nonNull)
                    .map(String::trim)
                    .filter(s -> !s.isEmpty())
                    .map(String::toUpperCase)
                    .collect(Collectors.toList());
            opEmpty = (opList == null || opList.isEmpty()) ? 1 : 0;
        }

        List<String> ouCodes = null;
        Integer ouEmpty = 1;
        if (req.getOuCodes() != null && !req.getOuCodes().isEmpty()) {
            ouCodes = req.getOuCodes().stream()
                    .filter(Objects::nonNull)
                    .map(String::trim)
                    .filter(s -> !s.isEmpty())
                    .map(String::toUpperCase)
                    .distinct()
                    .collect(Collectors.toList());
            ouEmpty = (ouCodes == null || ouCodes.isEmpty()) ? 1 : 0;
        }
        
        List<String> apprs = null;
        Integer apprEmpty = 1;
        if (StringUtils.hasText(req.getApproation())) {
            String ap = req.getApproation().trim().toUpperCase();
            if ("BOTH".equals(ap)) {
                apprs = Arrays.asList("AFTER", "BEFORE");
            } else {
                apprs = Collections.singletonList(ap);
            }
            apprEmpty = (apprs == null || apprs.isEmpty()) ? 1 : 0;
        }

        
        return repository.searchByOperationQuery(
        		yymm,
                ver,
                ouCodes, ouEmpty,
                opList, opEmpty,
                apprs, apprEmpty
        );
    }

}
