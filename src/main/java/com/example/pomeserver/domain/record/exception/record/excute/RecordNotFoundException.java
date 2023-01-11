package com.example.pomeserver.domain.record.exception.record.excute;

import com.example.pomeserver.domain.record.exception.record.RecordException;
import com.example.pomeserver.domain.record.exception.record.RecordExceptionList;

public class RecordNotFoundException extends RecordException {
    public RecordNotFoundException() {
        super(
                RecordExceptionList.RECORD_NOT_FOUND.CODE,
                RecordExceptionList.RECORD_NOT_FOUND.httpStatus,
                RecordExceptionList.RECORD_NOT_FOUND.MESSAGE
        );
    }
}
