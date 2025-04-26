package com.fastcampus.projectboardadmin.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.BDDMockito.willDoNothing;

import com.fastcampus.projectboardadmin.domain.AdminAccount;
import com.fastcampus.projectboardadmin.domain.constant.RoleType;
import com.fastcampus.projectboardadmin.dto.AdminAccountDto;
import com.fastcampus.projectboardadmin.repository.AdminAccountRepository;
import java.security.Permission;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito.Then;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayName("비지니스 로직 - 어드민 회원")
@ExtendWith(MockitoExtension.class)
class AdminAccountServiceTest {

    @InjectMocks private AdminAccountService sut;

    @Mock private AdminAccountRepository adminAccountRepository;

    @DisplayName("존재하는 회원 ID를 검색하면, 회원 데이터를 Optional로 반환한다.")
    @Test
    void givenExistentUserId_whenSearching_thenReturnsOptionalUserData(){
        // Given
        String username = "bomi";
        given(adminAccountRepository.findById(username)).willReturn(Optional.of(createAdminAccount(username)));

        // When
        Optional<AdminAccountDto> result = sut.searchUser(username);

        // Then
        assertThat(result).isPresent();
        then(adminAccountRepository).should().findById(username);
    }

    @DisplayName("존재하지 않는 회원 ID를 검색하면, 비어있는 Optional을 반환한다.")
    @Test
    void givenNoneExistentUserId_whenSearching_thenReturnsOptionalUserData(){
        // Given
        String username = "6031";
        given(adminAccountRepository.findById(username)).willReturn(Optional.empty());

        // When
        Optional<AdminAccountDto> result = sut.searchUser(username);

        // Then
        assertThat(result).isEmpty();
        then(adminAccountRepository).should().findById(username);
    }

    @DisplayName("회원 정보를 입력하면, 새로운 회원 정보를 저장하여 가입시키고 해당 회원 데이터를 리턴한다.")
    @Test
    void givenUserParams_whenSaving_thenSavesAdminAccount(){
        // Given
        AdminAccount adminAccount = createSigningUpAdminAccount("bomi", Set.of(RoleType.ADMIN));
        given(adminAccountRepository.save(adminAccount)).willReturn(adminAccount);

        // When
        AdminAccountDto result = sut.saveUser(
            adminAccount.getUserId(),
            adminAccount.getUserPassword(),
            adminAccount.getRoleTypes(),
            adminAccount.getEmail(),
            adminAccount.getNickname(),
            adminAccount.getMemo()
        );

        // Then
        assertThat(result)
            .hasFieldOrPropertyWithValue("userId", adminAccount.getUserId())
            .hasFieldOrPropertyWithValue("userPassword", adminAccount.getUserPassword())
            .hasFieldOrPropertyWithValue("roleTypes", adminAccount.getRoleTypes())
            .hasFieldOrPropertyWithValue("email", adminAccount.getEmail())
            .hasFieldOrPropertyWithValue("nickname", adminAccount.getNickname())
            .hasFieldOrPropertyWithValue("memo", adminAccount.getMemo())
            .hasFieldOrPropertyWithValue("createdBy", adminAccount.getCreatedBy())
            .hasFieldOrPropertyWithValue("modifiedBy", adminAccount.getModifiedBy());
        then(adminAccountRepository).should().save(adminAccount);
    }

    @DisplayName("전체 어드민 회원을 조회한다.")
    @Test
    void givenNothing_whenSelectingAdminAccounts_thenReturnsAllAdminAccounts() {
        // Given
        given(adminAccountRepository.findAll()).willReturn(List.of());

        // When
        List<AdminAccountDto> result = sut.users();

        // Then
        assertThat(result).hasSize(0);
        then(adminAccountRepository).should().findAll();
    }

    @DisplayName("회원 ID를 입력하면, 회원을 삭제한다.")
    @Test
    void givenUserId_whenDeletingAdminAccount_thenDeletesAdminAccounts() {
        // Given
        String userId = "bomi";
        willDoNothing().given(adminAccountRepository).deleteById(userId);

        // When
        sut.deleteUser(userId);

        // Then
        then(adminAccountRepository).should().deleteById(userId);
    }

    private AdminAccount createAdminAccount(String username) {
        return createAdminAccount(username, Set.of(RoleType.USER), null);
    }

    private AdminAccount createSigningUpAdminAccount(String username, Set<RoleType> roleTypes) {
        return createAdminAccount(username, roleTypes, username);
    }

    private AdminAccount createAdminAccount(String username, Set<RoleType> roleTypes, String createdBy) {
        return AdminAccount.of(
                username,
                "password",
                roleTypes,
                "bomi@email.com",
                "nickname",
                "memo",
                createdBy
        );
    }

}