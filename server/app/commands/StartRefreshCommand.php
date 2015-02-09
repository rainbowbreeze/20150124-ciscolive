<?php

use Illuminate\Console\Command;
use Illuminate\Support\Facades\Cache;
use Illuminate\Support\Facades\Log;
use Illuminate\Support\Facades\Queue;

class StartRefreshCommand extends Command {

    /**
     * The console command name.
     *
     * @var string
     */
    protected $name = 'refresh:start';

    /**
     * The console command description.
     *
     * @var string
     */
    protected $description = 'Start the data refreshing routine.';

    /**
     * Create a new command instance.
     *
     * @return void
     */
    public function __construct() {
        parent::__construct();
    }

    /**
     * Execute the console command.
     *
     * @return mixed
     */
    public function fire() {
        //$seconds = $this->argument('seconds');

        $key = 'explorer:refreshing:status';

        Cache::forever($key, 'refreshing');

        Log::info('Starting queue');

        Queue::push(function ($job) use ($key) {
            if (Cache::get($key) == 'stop') {
                $job->delete();
                Log::info('Stopping queue');
                return;
            }

            $manager = new LocationManager();

            $count = $manager->saveLocations();

            Log::info("{$count} locations saved");

            $job->release(5);
        });
    }

    /**
     * Get the console command arguments.
     *
     * @return array
     */
    protected function getArguments() {
        return array(
                //array('seconds', InputArgument::REQUIRED, 'Every tot seconds.'),
        );
    }

    /**
     * Get the console command options.
     *
     * @return array
     */
    protected function getOptions() {
        return array(
                //array('example', null, InputOption::VALUE_OPTIONAL, 'An example option.', null),
        );
    }

}
