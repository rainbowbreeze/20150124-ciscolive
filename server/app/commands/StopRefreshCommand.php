<?php

use Illuminate\Console\Command;
use Illuminate\Support\Facades\Cache;
use Illuminate\Support\Facades\Log;

class StopRefreshCommand extends Command {

    /**
     * The console command name.
     *
     * @var string
     */
    protected $name = 'refresh:stop';

    /**
     * The console command description.
     *
     * @var string
     */
    protected $description = 'Command description.';

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
        $key = 'explorer:refreshing:status';
        Cache::forever($key, 'stop');
        
        Log::info('Invoked queue stop command');
    }

    /**
     * Get the console command arguments.
     *
     * @return array
     */
    protected function getArguments() {
        return array(
                //	array('example', InputArgument::REQUIRED, 'An example argument.'),
        );
    }

    /**
     * Get the console command options.
     *
     * @return array
     */
    protected function getOptions() {
        return array(
                //	array('example', null, InputOption::VALUE_OPTIONAL, 'An example option.', null),
        );
    }

}
