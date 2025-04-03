package br.com.gabxdev.Audit;

import br.com.gabxdev.commons.UserUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.AuditorAware;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class AuditorAwareImpl implements AuditorAware<Long> {

    private final UserUtil userUtil;

    @Override
    public Optional<Long> getCurrentAuditor() {
        return userUtil.getCurrentUserId();
    }
}
