# Branches

This repository contains the following branches that store the history of the
Trove4j source code:

* [svn-original](../../tree/svn-original)
* [svn-clean](../../tree/svn-clean)
* [bitbucket-original](../../tree/bitbucket-original)
* [bitbucket-clean](../../tree/bitbucket-clean)
* [full-history](../../tree/full-history)


## svn-original

This is the original source repository as imported from the SourceForge SVN
repository at <https://svn.code.sf.net/p/trove4j/code/>.


## svn-clean

This is the same as `svn-original` with the newlines converted from DOS to Unix as
per:

    git filter-branch --tree-filter "find . -type f | xargs dos2unix"


## bitbucket-original

This is a mirror of the master branch from the BitBucket repository at
<https://bitbucket.org/trove4j/trove.git>.


## bitbucket-clean

This is the same as `bitbucket-original` with the newlines converted from DOS to Unix as
per:

    git filter-branch --tree-filter "find . -type f | xargs dos2unix"

And the permissions of all files fixed, such that the executable bit is not set
on most regular files as per:

    git filter-branch --tree-filter "find . -type f | xargs chmod 644"


## full-history

This branch contains the history of `svn-clean` plus the history from
`bitbucket-clean` applied on top. It has been set up like this:

    git checkout bitbucket-clean
    git checkout -b full-history
    git rebase --onto svn-clean --root --keep-empty --preserve-merges

This will stop with the following message:

    The previous cherry-pick is now empty, possibly due to conflict resolution.
    ...
    pick 609d1f4 Imported from SVN by Bitbucket

We'll continue like this:

    git commit --allow-empty
    git rebase --continue

Afterwards, fix the committer dates:

    git filter-branch --env-filter 'export GIT_COMMITTER_DATE="$GIT_AUTHOR_DATE"'
