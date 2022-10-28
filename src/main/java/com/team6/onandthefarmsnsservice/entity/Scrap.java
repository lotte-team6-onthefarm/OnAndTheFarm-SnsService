package com.team6.onandthefarmsnsservice.entity;

import lombok.*;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.*;

@Builder
@Slf4j
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@SequenceGenerator(
        name="SCRAP_SEQ_GENERATOR",
        sequenceName = "SCRAP_SEQ",
        initialValue = 100000, allocationSize = 1
)
public class Scrap {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
            generator = "SCRAP_SEQ_GENERATOR")
    private Long scrapId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "feedId")
    private Feed feed;

    private Long memberId;
}
