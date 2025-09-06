# [2.4.0](https://github.com/arkitik/travako/compare/v2.3.4...v2.4.0) (2025-09-06)


### Bug Fixes

* add publishEvent flag to job registration and update Redis configuration ([366a663](https://github.com/arkitik/travako/commit/366a663b6a63cde31644d561ef3df27b845fb5e9))
* enhance version calculation to determine bump type based on commit messages ([f17444d](https://github.com/arkitik/travako/commit/f17444d2aafea255b493dba1dc7d668d3462f1e1))
* handle cases with no existing tags in version calculation ([20a4c36](https://github.com/arkitik/travako/commit/20a4c3633d8ab32aad99b9eded6f31d613a1f815))
* improve latest tag retrieval and version regex matching ([968a5ef](https://github.com/arkitik/travako/commit/968a5ef0342b9bac78751ec777113ad6e8b62c20))
* replace full host address with only hostname in TravakoRunnerConfig ([0079fe0](https://github.com/arkitik/travako/commit/0079fe02580fbfc3ebc03a0b7264afb2ff615996))


### Features

* add default naming strategy for Travako plugin ([ec2be8d](https://github.com/arkitik/travako/commit/ec2be8d427b354b9376b2f69c8b4cbbddb6c6114))
* add exposed entities implementations [#23](https://github.com/arkitik/travako/issues/23) ([f8c1c10](https://github.com/arkitik/travako/commit/f8c1c108abc11492527e87cbae77d7aa038275fa))
* implement Redis-based entities and repositories for Travako ([cac73ab](https://github.com/arkitik/travako/commit/cac73ab80f0e4965615a2e6bb2f729378b24681a))
* implement stateful job execution and registry [#45](https://github.com/arkitik/travako/issues/45) ([f2c03d0](https://github.com/arkitik/travako/commit/f2c03d0eeeec65fee1f6107a57bdf15e1690fa15))


### Reverts

* revert redis as its implementation will require more effort to meet the standard redis usages ([ae20145](https://github.com/arkitik/travako/commit/ae20145ed6824f0b693384429be0131e8db1184a))
