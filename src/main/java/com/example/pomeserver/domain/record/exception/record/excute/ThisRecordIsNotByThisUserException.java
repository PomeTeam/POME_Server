package com.example.pomeserver.domain.record.exception.record.excute;

import com.example.pomeserver.domain.record.exception.record.RecordException;
import com.example.pomeserver.domain.record.exception.record.RecordExceptionList;

public class ThisRecordIsNotByThisUserException extends RecordException {
    public ThisRecordIsNotByThisUserException() {
        super(
                RecordExceptionList.THIS_RECORD_NOT_BY_THIS_USER.CODE,
                RecordExceptionList.THIS_RECORD_NOT_BY_THIS_USER.httpStatus,
                RecordExceptionList.THIS_RECORD_NOT_BY_THIS_USER.MESSAGE
        );
    }
}
