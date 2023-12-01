package net.guzari.search.rest.controller;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import net.guzari.search.domain.report.ReportService;
import net.guzari.search.rest.mapper.ReportDtoMapper;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.Instant;
import java.util.Date;
import java.util.Optional;

import static net.guzari.search.rest.controller.ReportDataReceiver.*;
import static net.guzari.search.rest.exceptions.ExceptionUtil.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest(classes = TestConfiguration.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class ReportControllerTest {
    public static final String EMAIL = "test@gmail.com";
    public static final String BEARER = "Bearer ";
    public static final String JWT_SIGNING_KEY = "secret";
    public static final Long EXPIRATION = 60_000L;
    private static String TOKEN;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ReportService reportService;

    @MockBean
    private ReportDtoMapper mapper;

    @BeforeEach
    public void setUp() {
        TOKEN = getBearerToken(new Date());
    }

    private static String getBearerToken(Date date) {
        Date expiryDate = new Date(date.getTime() + EXPIRATION);
        return BEARER + Jwts.builder()
                .setSubject(EMAIL)
                .setIssuedAt(date)
                .setExpiration(expiryDate)
                .signWith(SignatureAlgorithm.HS512, JWT_SIGNING_KEY)
                .compact();
    }

    @Test
    void givenEmptyAuthorizationHeader_findReports_thenThrowValidationException() throws Exception {
        mockMvc.perform(get("/api/report/search")
                        .queryParam("keywords", KEYWORDS)
                        .header("Authorization", ""))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError())
                .andExpect(jsonPath("$.code", Matchers.is(401)))
                .andExpect(jsonPath("$.message", Matchers.is(AUTH_HEADER_EXCEPTION.getMessage())));
    }

    @Test
    void givenExpiredToken_findReports_thenThrowValidationException() throws Exception {
        Date date = Date.from(Instant.now().minusSeconds(70));
        mockMvc.perform(get("/api/report/search")
                        .queryParam("keywords", KEYWORDS)
                        .header("Authorization", getBearerToken(date)))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError())
                .andExpect(jsonPath("$.code", Matchers.is(401)))
                .andExpect(jsonPath("$.message", Matchers.is(EXPIRED_TOKEN_EXCEPTION.getMessage())));
    }

    @Test
    void givenInvalidToken_findReports_thenThrowValidationException() throws Exception {
        mockMvc.perform(get("/api/report/search")
                        .queryParam("keywords", KEYWORDS)
                        .header("Authorization", TOKEN + "12"))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError())
                .andExpect(jsonPath("$.code", Matchers.is(401)))
                .andExpect(jsonPath("$.message", Matchers.is(INVALID_TOKEN_EXCEPTION.getMessage())));
    }

    @Test
    void givenKeywords_findReports_thenReturnReports() throws Exception {
        when(reportService.findReports(KEYWORDS)).thenReturn(getReports());
        when(mapper.toDto(getReport1())).thenReturn(getReportDto());
        when(mapper.toDto(getReport2())).thenReturn(getReportDto2());
        mockMvc.perform(get("/api/report/search")
                        .queryParam("keywords", KEYWORDS)
                        .header("Authorization", TOKEN))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$", Matchers.hasSize(2)))
                .andExpect(jsonPath("$.[0].id", Matchers.is(ID_1)))
                .andExpect(jsonPath("$.[0].title", Matchers.is(TITLE)))
                .andExpect(jsonPath("$.[0].authors", Matchers.is(AUTHORS)))
                .andExpect(jsonPath("$.[0].target", Matchers.is(TARGET)))
                .andExpect(jsonPath("$.[0].content", Matchers.is(KEYWORDS)))
                .andExpect(jsonPath("$.[0].created", Matchers.is(DATE)));
    }

    @Test
    void givenNoReportsByKeywords_findReports_thenReturnEmptyReportsList() throws Exception {
        mockMvc.perform(get("/api/report/search")
                        .queryParam("keywords", KEYWORDS)
                        .header("Authorization", TOKEN))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    void givenEmptyKeywords_findReports_thenThrowValidationException() throws Exception {
        mockMvc.perform(get("/api/report/search")
                        .queryParam("keywords", "")
                        .header("Authorization", TOKEN))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError())
                .andExpect(jsonPath("$.code", Matchers.is(400)))
                .andExpect(jsonPath("$.message", Matchers.is(VALIDATION_EXCEPTION.getMessage())));
    }

    @Test
    void givenTooLongKeywords_findReports_thenThrowValidationException() throws Exception {
        when(reportService.findReports(KEYWORDS)).thenReturn(getReports());
        mockMvc.perform(get("/api/report/search")
                        .queryParam("keywords", LONG_KEYWORDS)
                        .header("Authorization", TOKEN))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError())
                .andExpect(jsonPath("$.code", Matchers.is(400)))
                .andExpect(jsonPath("$.message", Matchers.is(VALIDATION_EXCEPTION.getMessage())));
    }

    @Test
    void givenKeywords_reportAutocomplete_thenReturnReportIdAndTitleDtoList() throws Exception {
        when(reportService.reportAutocomplete(KEYWORDS)).thenReturn(getReports());
        when(mapper.toIdAndTitleDto(getReport1())).thenReturn(getIdAndTitleDto());
        when(mapper.toIdAndTitleDto(getReport2())).thenReturn(getIdAndTitleDto2());
        mockMvc.perform(get("/api/report/autocomplete")
                        .queryParam("keywords", KEYWORDS)
                        .header("Authorization", TOKEN))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$", Matchers.hasSize(2)))
                .andExpect(jsonPath("$.[0].id", Matchers.is(ID_1)))
                .andExpect(jsonPath("$.[0].title", Matchers.is(TITLE)))
                .andExpect(jsonPath("$.[1].id", Matchers.is(ID_2)))
                .andExpect(jsonPath("$.[1].title", Matchers.is(TITLE)));
    }

    @Test
    void givenNoReportsByKeywords_reportAutocomplete_thenReturnEmptyReportIdAndTitleDtoList() throws Exception {
        mockMvc.perform(get("/api/report/autocomplete")
                        .queryParam("keywords", KEYWORDS)
                        .header("Authorization", TOKEN))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    void givenEmptyKeywords_reportAutocomplete_thenThrowValidationException() throws Exception {
        mockMvc.perform(get("/api/report/autocomplete")
                        .queryParam("keywords", "")
                        .header("Authorization", TOKEN))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError())
                .andExpect(jsonPath("$.code", Matchers.is(400)))
                .andExpect(jsonPath("$.message", Matchers.is(VALIDATION_EXCEPTION.getMessage())));
    }

    @Test
    void givenLongKeywords_reportAutocomplete_thenThrowValidationException() throws Exception {
        mockMvc.perform(get("/api/report/autocomplete")
                        .queryParam("keywords", LONG_KEYWORDS)
                        .header("Authorization", TOKEN))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError())
                .andExpect(jsonPath("$.code", Matchers.is(400)))
                .andExpect(jsonPath("$.message", Matchers.is(VALIDATION_EXCEPTION.getMessage())));
    }

    @Test
    void givenReportId_findById_thenReturnReport() throws Exception {
        when(reportService.findById(ID_1)).thenReturn(Optional.ofNullable(getReport1()));
        when(mapper.toDto(getReport1())).thenReturn(getReportDto());
        mockMvc.perform(get("/api/report/" + ID_1)
                        .header("Authorization", TOKEN))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.id", Matchers.is(ID_1)))
                .andExpect(jsonPath("$.title", Matchers.is(TITLE)))
                .andExpect(jsonPath("$.authors", Matchers.is(AUTHORS)))
                .andExpect(jsonPath("$.target", Matchers.is(TARGET)))
                .andExpect(jsonPath("$.content", Matchers.is(KEYWORDS)))
                .andExpect(jsonPath("$.created", Matchers.is(DATE)));
    }

    @Test
    void givenNotExistingId_findById_thenThrowInternalServerError() throws Exception {
        mockMvc.perform(get("/api/report/" + ID_1)
                        .header("Authorization", TOKEN))
                .andExpect(MockMvcResultMatchers.status().is5xxServerError())
                .andExpect(jsonPath("$.code", Matchers.is(500)))
                .andExpect(jsonPath("$.message", Matchers.is(INTERNAL_SERVER_ERROR.getMessage())));
    }

}
