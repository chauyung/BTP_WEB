/**
 * 
 */
package nccc.btp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import nccc.btp.entity.BpmNcccMissionAssigner;

/**
 * 
 */
public interface BpmNcccMissionAssignerRepository
    extends JpaRepository<BpmNcccMissionAssigner, String> {
  BpmNcccMissionAssigner findByOuCode(String ouCode);
}
