package com.example.pomeserver.domain.record.exception.record.excute;

import com.example.pomeserver.domain.record.exception.record.RecordException;
import com.example.pomeserver.domain.record.exception.record.RecordExceptionList;

public class ThisRecordIsNotByThisUser extends RecordException {
    public ThisRecordIsNotByThisUser() {
        super(
                RecordExceptionList.THIS_RECORD_NOT_BY_THIS_USER.CODE,
                RecordExceptionList.THIS_RECORD_NOT_BY_THIS_USER.httpStatus,
                RecordExceptionList.THIS_RECORD_NOT_BY_THIS_USER.MESSAGE
        );
    }
}
