package com.yff.base.mapper;

import com.yff.base.domain.EmailActive;

public interface EmailActiveMapper {
    int deleteByPrimaryKey(Long id);

    int insert(EmailActive record);

    int insertSelective(EmailActive record);

    EmailActive selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(EmailActive record);

    int updateByPrimaryKey(EmailActive record);

    EmailActive selectByUuid(String uuid);
}