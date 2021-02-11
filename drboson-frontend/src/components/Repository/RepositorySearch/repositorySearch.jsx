import React, { useState } from 'react';
import AsyncSelect from 'react-select/async';
import RepoService from '../../../actions/repo';

const RepositorySearch = (props) => {


    const fetchRepos = (owner) => {
        if (!owner.includes('/')) {
            return Promise.resolve();
        }

        return RepoService.fetchOwnerRepos(owner.split('/')[0])
            .then(response => response.data.map(repo => {
                return {
                    value: repo,
                    label: repo.full_name
                }
            }));
    };

    const repoFilter = (candidate, input) => {
        // console.log(candidate)
        // console.log(input)
        // return true;
        return candidate.value.full_name.toLowerCase().startsWith(input.toLowerCase());
    };

    return (
        <AsyncSelect filterOption={repoFilter} loadOptions={fetchRepos} />
    );
};

export default RepositorySearch;