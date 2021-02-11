import React from 'react';
import { useSnackbar as useDefaultSnackbar, OptionsObject } from 'notistack';
import Notification from './notification';

export const useEnqueueSnackbar = () => {
    const { enqueueSnackbar } = useDefaultSnackbar();

    const pushSnackbar = (message, options) => {
        enqueueSnackbar(message, {
            ...options,
            content: (key) => {
                const { variant, onUndo, undoId } = options || { variant: undefined };
                return (
                    <Notification
                        id={key}
                        message={message}
                        variant={variant}
                    />
                );
            },
        });
    };

    return pushSnackbar;
};