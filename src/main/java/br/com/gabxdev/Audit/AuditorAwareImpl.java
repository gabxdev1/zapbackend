package br.com.gabxdev.Audit;

import br.com.gabxdev.commons.AuthUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.AuditorAware;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class AuditorAwareImpl implements AuditorAware<Long> {

    private final AuthUtil authUtil;

    @Override
    public Optional<Long> getCurrentAuditor() {
        return authUtil.getCurrentUserId();
    }
}
